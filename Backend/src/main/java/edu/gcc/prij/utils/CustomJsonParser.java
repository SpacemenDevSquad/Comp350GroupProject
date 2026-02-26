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
import edu.gcc.prij.utils.time.Timeslot;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CustomJsonParser {
    private Repository<Section, SectionKey> sectionRepository;
    private Repository<Department, String> departmentRepository;
    private Repository<Course, CourseKey> courseRepository;
    private Repository<Professor, String> professorRepository;

    public CustomJsonParser(
        Repository<Section, SectionKey> sectionRepository,
        Repository<Department, String> departmentRepository,
        Repository<Course, CourseKey> courseRepository,
        Repository<Professor, String> professorRepository
    ){
        this.sectionRepository = sectionRepository;
        this.departmentRepository = departmentRepository;
        this.courseRepository = courseRepository;
        this.professorRepository = professorRepository;
    }

    public void parse() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            InputStream inputStream = CustomJsonParser.class.getResourceAsStream("/data_wolfe.json");

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

                char sectionLetter = c.get("section").asText().charAt(0);
                String semesterString = c.get("semester").asText();

                ArrayList<Professor> profs = new ArrayList<Professor>();

                JsonNode professors = c.get("faculty");

                for (JsonNode p : professors){
                    Professor professor = professorRepository.getOrAdd(p.asText(), new Professor(p.asText()));
                    profs.add(professor);
                }

                ArrayList<Timeslot> times = new ArrayList<Timeslot>();

                JsonNode timeslots = c.get("times");

                for (JsonNode t : timeslots){
                    times.add(new Timeslot(
                        t.get("start_time").asText(),
                        t.get("end_time").asText(),
                        t.get("day").asText().charAt(0)
                    ));
                }

                Semester semester = Semester.getSemesterFromString(semesterString);
                SectionKey sectionKey = new SectionKey(course, sectionLetter, semester);
                Section section = new Section(course, sectionLetter, times.toArray(new Timeslot[0]), profs.toArray(new Professor[0]), semester);

                sectionRepository.getOrAdd(sectionKey, section);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}