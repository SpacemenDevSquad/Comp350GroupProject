package edu.gcc.prij.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.gcc.prij.objects.course.Course;
import edu.gcc.prij.objects.course.CourseKey;
import edu.gcc.prij.objects.department.Department;
import edu.gcc.prij.objects.professor.Professor;
import edu.gcc.prij.objects.section.Section;
import edu.gcc.prij.objects.section.SectionKey;
import edu.gcc.prij.objects.semester.Semester;
import edu.gcc.prij.objects.semester.SemesterKey;
import edu.gcc.prij.utils.time.Timeslot;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CustomJsonParser {
    private String path;
    private Repository<Section, SectionKey> sectionRepository;
    private Repository<Department, String> departmentRepository;
    private Repository<Course, CourseKey> courseRepository;
    private Repository<Professor, String> professorRepository;
    private Repository<Semester, SemesterKey> semesterRepository;

    public CustomJsonParser(
        String path,
        Repository<Section, SectionKey> sectionRepository,
        Repository<Department, String> departmentRepository,
        Repository<Course, CourseKey> courseRepository,
        Repository<Professor, String> professorRepository,
        Repository<Semester, SemesterKey> semesterRepository
    ){
        this.path = path;
        this.sectionRepository = sectionRepository;
        this.departmentRepository = departmentRepository;
        this.courseRepository = courseRepository;
        this.professorRepository = professorRepository;
        this.semesterRepository = semesterRepository;
    }

    public void parse() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            InputStream inputStream = CustomJsonParser.class.getResourceAsStream(path);

            JsonNode rootNode = mapper.readTree(inputStream);

            JsonNode classes = rootNode.get("classes");

            for (JsonNode c : classes){
                String subject = c.get("subject").asText();
                Department dept = departmentRepository.getOrAdd(subject, new Department(subject));
                int number = c.get("number").asInt();
                String title = c.get("name").asText();
                int credits = c.get("credits").asInt();
            
                CourseKey courseKey = new CourseKey(dept, number);
                Course fallbackCourse = new Course(dept, number, title, null, credits);
                Course course = courseRepository.getOrAdd(courseKey, fallbackCourse);

                String sectionText = c.has("section") ? c.get("section").asText() : "";
                if (sectionText.isEmpty()) {
                    System.out.println("Skipped class: subject=" + subject + ", number=" + number + " (invalid/missing section)");
                    continue;
                }
                char sectionLetter = sectionText.charAt(0);
                String semesterString = c.get("semester").asText();

                ArrayList<Professor> profs = new ArrayList<Professor>();

                JsonNode professorsNode = c.get("faculty");
                if (professorsNode != null && professorsNode.isArray()) {
                    for (JsonNode p : professorsNode) {
                        String profName = p.asText();
                        if (profName != null && !profName.trim().isEmpty()) {
                            Professor professor = professorRepository.getOrAdd(profName.trim(), new Professor(profName.trim()));
                            profs.add(professor);
                        }
                    }
                }

                ArrayList<Timeslot> times = new ArrayList<Timeslot>();

                JsonNode timeslotsNode = c.get("times");
                if (timeslotsNode != null && timeslotsNode.isArray()) {
                    for (JsonNode t : timeslotsNode) {
                        String dayText = t.has("day") ? t.get("day").asText() : "";
                        if (!dayText.isEmpty() && t.has("start_time") && t.has("end_time")) {
                            times.add(new Timeslot(
                                t.get("start_time").asText(),
                                t.get("end_time").asText(),
                                dayText.charAt(0)
                            ));
                        } else {
                            System.out.println("Skipped timeslot for class " + subject + " " + number + " (invalid/missing day or times)");
                        }
                    }
                }

                Semester fallbackSemester = Semester.getSemesterFromString(semesterString);

                Semester semester = null;

                if(fallbackSemester != null){
                    semester = semesterRepository.getOrAdd(
                        fallbackSemester.getKey(),
                        fallbackSemester
                    );
                }
                
                SectionKey sectionKey = new SectionKey(course, sectionLetter, semester);
                Section section = new Section(course, sectionLetter, times.toArray(new Timeslot[0]), profs.toArray(new Professor[0]), semester);

                sectionRepository.upsert(sectionKey, section);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}