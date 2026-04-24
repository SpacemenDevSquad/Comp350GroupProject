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
import java.util.List;


// SCHEDULE CONTROLLER CLASS: Manages API endpoints for creating, retrieving, and modifying student schedules.
public class ScheduleController implements Controller {

    // ---- Initialize Repositories ----
    private Repository<Section, SectionKey> sectionRepo;
    private Repository<Department, String> deptRepo;
    private Repository<Course, CourseKey> courseRepo;
    private Repository<Schedule, ScheduleKey> scheduleRepository;
    private Repository<Semester, SemesterKey> semesterRepository;
    private Repository<User, String> userRepository;
    
    // ---- CONSTRUCTOR ----
    // standard constructor (initizlzies all the repositories used to fill schedule)
    public ScheduleController(
        Repository<Section, SectionKey> sectionRepo,
        Repository<Department, String> deptRepo,
        Repository<Course, CourseKey> courseRepo,
        Repository<Schedule, ScheduleKey> scheduleRepository,
        Repository<Semester, SemesterKey> semesterRepository,
        Repository<User, String> userRepository)
    {
        this.sectionRepo = sectionRepo;
        this.courseRepo = courseRepo;
        this.deptRepo = deptRepo;
        this.scheduleRepository = scheduleRepository;
        this.semesterRepository = semesterRepository;
        this.userRepository = userRepository;
    }

    // ---- HELPER METHODS ----

    // Parses Javalin path parameters to get/initialize a schedule (parses)
    private Schedule getOrAddSchedule(Context ctx){
        String userId = ctx.pathParam("userId");
        int year = Integer.parseInt(ctx.pathParam("year"));
        char term = ctx.pathParam("term").charAt(0);
        String scheduleName = ctx.pathParam("scheduleName");

        return getOrAddSchedule(userId, year, term, scheduleName);
    }

    //ensures variables actually exist in the repositories (doesn't parse) 
    private Schedule getOrAddSchedule(String userId, int year, char term, String scheduleName){
        User user = userRepository.getOrAdd(userId, new User(userId, "unknown@gcc.edu", "New User"));
        Semester sem = semesterRepository.getOrAdd(new SemesterKey(year, term), new Semester(year, term));
        
        ScheduleKey key = new ScheduleKey(user, sem, scheduleName);
        return scheduleRepository.getOrAdd(key, new Schedule(user, sem, scheduleName));
    }

    

    // ---- ROUTE REGISTRATION ----

    public void registerRoutes(Javalin app){
        
        // GET: Retreives the schedule
        app.get("/api/schedule/{userId}/{year}/{term}/{scheduleName}", ctx -> {
            //Gets a current schedule or makes a new one based on ScheduleKey
            Schedule sch = getOrAddSchedule(ctx);

            // Returns the full schedule object as JSON for the WeeklyView component
            ctx.json(sch);
        });

        // GET: Retrieves total credits
        app.get("/api/schedule/credits/{userId}/{year}/{term}/{scheduleName}", ctx -> {
            //Gets a current schedule or makes a new one based on ScheduleKey
            Schedule sch = getOrAddSchedule(ctx);

            // Returns just the integer of current credits
            ctx.json(sch.currentCredits());
        });
        
        //GET: Retrieves all the schedule names for the dropdown list
        app.get("/api/schedules/{userId}/{year}/{term}", ctx -> {
            String userId = ctx.pathParam("userId");
            int year = Integer.parseInt(ctx.pathParam("year"));
            char term = ctx.pathParam("term").charAt(0);

            // Filters the repository for all schedules matching this user/semester
            List<String> names = scheduleRepository.findAll().stream()
                .filter(s -> s.getUser().getId().equals(userId) && 
                            s.getSemester().getYear() == year && 
                            s.getSemester().getTerm() == term)
                .map(Schedule::getName)
                .distinct()
                .toList();
            
            ctx.json(names);
        });



        // POST: Add a section to the schedule
        app.post("/api/schedule/add/{userId}/{year}/{term}/{scheduleName}", ctx -> {
            
            // Check for the "force" query param (used when user confirms overriding 18-credit limit)
            boolean force = Boolean.parseBoolean(ctx.queryParam("force"));
            System.out.println("ADD Request received for user: " + ctx.pathParam("userId")); 

            try{
                Schedule sch = getOrAddSchedule(ctx);

                // Use Javalin to turn incoming JSON to a Section object
                Section newSection = ctx.bodyAsClass(Section.class);
                
                //adds section to schedule if there is no conflict
                String result = sch.addSection(newSection, force);
                System.out.println("addSection result for " + newSection.getCourse().getDepartment().getCode() + newSection.getCourse().getNumber() + newSection.getSectionLetter() + ": " + result);

                //If added successfully or forced, update the Schedule Repository accordingly (201=Success)
                if (result.equals("ADD") || (result.equals("CREDIT_LIMIT")  && force)) {
                    scheduleRepository.update(sch.getKey(), sch);
                    ctx.status(201).json(sch);
                } 
                // If time conflict (409= Conflict)
                else if (result.equals("CONFLICT"))  {
                    ctx.status(409).result("Conflict detected!");
                }
                else if (result.equals("DUPLICATE")) {
                    ctx.status(409).result("This course is already in your schedule.");
                }
                // If hit credit limit (403= access denied)
                else{
                    ctx.status(403).result("CREDIT_LIMIT");
                }
                        
            } catch (Exception e){
                e.printStackTrace();

                ctx.status(500).result("Java Crash: " + e.getMessage());
            }
        });

        //DELETE: Delete section from the schedule
        app.delete("/api/schedule/drop/{userId}/{year}/{term}/{scheduleName}", ctx ->{

            System.out.println("DELETE Request received for user: " + ctx.pathParam("userId"));

            try{
                // Get the Section object of the Delete request
                Section fallbackSection = ctx.bodyAsClass(Section.class);
                System.out.println(fallbackSection);

                // Looks up the section in repository to ensure its valid section
                Section section = sectionRepo.findById(fallbackSection.getKey());
                
                Schedule sch = getOrAddSchedule(ctx);

                // Attempts to remove course and update repository if successful
                if (sch != null && section != null) {
                    // Call the updated method and check the return value
                    boolean removed = sch.dropSection(section);
                    
                    //If successful removal (200=success)
                    if (removed) {
                        scheduleRepository.update(sch.getKey(), sch);
                        ctx.status(200).json(sch); // 200 is more standard for deletion than 201
                    } 
                    // if the course exists in sectionRepo but not in the user's schedule (404=request not found)
                    else {

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
        // DELETE: Deletes an entire schedule from the repository
        app.delete("/api/schedule/delete/{userId}/{year}/{term}/{scheduleName}", ctx -> {
            String userId = ctx.pathParam("userId");
            int year = Integer.parseInt(ctx.pathParam("year"));
            char term = ctx.pathParam("term").charAt(0);
            String scheduleName = ctx.pathParam("scheduleName");

            try {
                // Find the User and Semester to build the composite key
                User user = userRepository.findById(userId);
                Semester sem = semesterRepository.findById(new SemesterKey(year, term));

                if (user != null && sem != null) {
                    ScheduleKey key = new ScheduleKey(user, sem, scheduleName);
                    
                    // Attempt to delete from the repository
                    boolean deleted = scheduleRepository.deleteById(key);
                    
                    if (deleted) {
                        ctx.status(200).result("Schedule deleted successfully.");
                    } else {
                        ctx.status(404).result("Schedule not found.");
                    }
                } else {
                    ctx.status(404).result("User or Semester not found.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500).result("Backend Error: " + e.getMessage());
            }
        });

    }
}