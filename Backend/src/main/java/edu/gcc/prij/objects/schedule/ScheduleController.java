package edu.gcc.prij.objects.schedule;
import edu.gcc.prij.objects.user.User;

import java.util.HashMap;
import java.util.Map;

import edu.gcc.prij.objects.course.Course;
import edu.gcc.prij.objects.course.CourseKey;
import edu.gcc.prij.objects.section.Section;
import edu.gcc.prij.objects.section.SectionKey;
import edu.gcc.prij.objects.semester.Semester;
import edu.gcc.prij.objects.semester.SemesterKey;
import edu.gcc.prij.objects.department.Department;
import edu.gcc.prij.utils.Controller;
import edu.gcc.prij.utils.Repository;
import io.javalin.Javalin;
import io.javalin.http.Context;

public class ScheduleController implements Controller {

    private Repository<Section, SectionKey> sectionRepo;
    private Repository<Department, String> deptRepo;
    private Repository<Course, CourseKey> courseRepo;
    private Repository<Schedule, ScheduleKey> scheduleRepository;
    private Repository<Semester, SemesterKey> semesterRepository;
    private Repository<User, Integer> userRepository;
    
    //CONSTRUCTOR (used to load the in-memory sectionRepository)
    public ScheduleController(
        Repository<Section, SectionKey> sectionRepo,
        Repository<Department, String> deptRepo,
        Repository<Course, CourseKey> courseRepo,
        Repository<Schedule, ScheduleKey> scheduleRepository,
        Repository<Semester, SemesterKey> semesterRepository,
        Repository<User, Integer> userRepository
    )
    {
        this.sectionRepo = sectionRepo;
        this.courseRepo = courseRepo;
        this.deptRepo = deptRepo;
        this.scheduleRepository = scheduleRepository;
        this.semesterRepository = semesterRepository;
        this.userRepository = userRepository;
    }

    private Schedule getOrAddSchedule(Context ctx){
        int userId = Integer.parseInt(ctx.pathParam("userId"));
        int year = Integer.parseInt(ctx.pathParam("year"));
        char term = ctx.pathParam("term").charAt(0);

        return getOrAddSchedule(userId, year, term);
    }

    private Schedule getOrAddSchedule(int userId, int year, char term){
        User fallbackUser = new User(userId, "merrickrw23@gcc.edu", "Ryan Merrick");
        User user = userRepository.getOrAdd(userId, fallbackUser);

        Semester fallbackSemester = new Semester(
            year,
            term
        );
        Semester sem = semesterRepository.getOrAdd(
            fallbackSemester.getKey(),
            fallbackSemester
        );

        ScheduleKey key = new ScheduleKey(user, sem);

        //Gets a current schedule or makes a new one based on ScheduleKey
        return scheduleRepository.getOrAdd(key, new Schedule(user, sem));
    }

    public void registerRoutes(Javalin app){
        
        // GET: Retreives the schedule
        app.get("/api/schedule/{userId}/{year}/{term}", ctx -> {
            //Gets a current schedule or makes a new one based on ScheduleKey
            Schedule sch = getOrAddSchedule(ctx);

            ctx.json(sch);
        });

        // GET: Retrieves total credits
        app.get("/api/schedule/credits/{userId}/{year}/{term}", ctx -> {
            //Gets a current schedule or makes a new one based on ScheduleKey
            Schedule sch = getOrAddSchedule(ctx);

            ctx.json(sch.currentCredits());
        });



        // POST: Add a section to the schedule
        app.post("/api/schedule/add/{userId}/{year}/{term}", ctx -> {
            
            //force add if over 18 credits
            boolean force = Boolean.parseBoolean(ctx.queryParam("force"));

            System.out.println("ADD Request received for user: " + ctx.pathParam("userId")); 

            try{
                Schedule sch = getOrAddSchedule(ctx);

                Section newSection = ctx.bodyAsClass(Section.class);
                
                //adds section to schedule if there is no conflict
                String result = sch.addSection(newSection);
                System.out.println("addSection result for " + newSection.getCourse().getDepartment().getCode() + newSection.getCourse().getNumber() + newSection.getSectionLetter() + ": " + result);
                if (result.equals("ADD") || (result.equals("CREDIT_LIMIT")  && force)) {
                    System.out.println("About to update repo for schedule key: " + sch.getKey());
                    scheduleRepository.update(sch.getKey(), sch);
                    System.out.println("Repo update complete");
                    ctx.status(201).json(sch);
                } 
                else if (result.equals("CONFLICT"))  {
                    ctx.status(409).result("Conflict detected!");
                }
                else{
                    ctx.status(403).result("CREDIT_LIMIT");
                }
                        
            } catch (Exception e){
                e.printStackTrace();

                ctx.status(500).result("Java Crash: " + e.getMessage());
            }
        });

        //DELETE: Delete section from the schedule
        app.delete("/api/schedule/drop/{userId}/{year}/{term}", ctx ->{

            System.out.println("DELETE Request received for user: " + ctx.pathParam("userId"));

            try{
                Section fallbackSection = ctx.bodyAsClass(Section.class);

                System.out.println(fallbackSection);

                Section section = sectionRepo.findById(fallbackSection.getKey());
                
                // If schedule doesn't exist, exit
                Schedule sch = getOrAddSchedule(ctx);

                //removes section from schedule
                if (sch != null && section != null) {
                    // Call the updated method and check the return value
                    boolean removed = sch.dropSection(section);
                    
                    if (removed) {
                        scheduleRepository.update(sch.getKey(), sch);
                        ctx.status(200).json(sch); // 200 is more standard for deletion than 201
                    } else {
                        // This triggers if the course exists in sectionRepo but NOT in the user's list
                        ctx.status(404).result("This course is not currently in your schedule.");
                    }
                } else {
                    ctx.status(404).result("Section or Schedule not found.");
                }

            }catch (Exception e){
                e.printStackTrace();
                ctx.status(500).result("Error: " + e.getMessage());
            }
        });

    }
}