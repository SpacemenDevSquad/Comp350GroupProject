package edu.gcc.prij.objects.schedule;
import edu.gcc.prij.objects.user.User;

import java.util.HashMap;
import java.util.Map;

import edu.gcc.prij.objects.course.Course;
import edu.gcc.prij.objects.course.CourseKey;
import edu.gcc.prij.objects.section.Section;
import edu.gcc.prij.objects.section.SectionKey;
import edu.gcc.prij.objects.semester.Semester;
import edu.gcc.prij.objects.department.Department;
import edu.gcc.prij.utils.Controller;
import edu.gcc.prij.utils.Repository;
import io.javalin.Javalin;

public class ScheduleController implements Controller {

    private Repository<Section, SectionKey> sectionRepo;
    private Repository<Department, String> deptRepo;
    private Repository<Course, CourseKey> courseRepo;

    //stores schedule
    private final Map<ScheduleKey, Schedule> userSchedules = new HashMap<>();
    
    //CONSTRUCTOR (used to load the in-memory sectionRepository)
    public ScheduleController(Repository<Section, SectionKey> sectionRepo, Repository<Department, String> deptRepo, Repository<Course, CourseKey> courseRepo) {
        this.sectionRepo = sectionRepo;
        this.courseRepo = courseRepo;
        this.deptRepo = deptRepo;
    }

    public void registerRoutes(Javalin app){
        
        // GET: Retreives the schedule
        app.get("/api/schedule/{userId}", ctx -> {
            int userId = Integer.parseInt(ctx.pathParam("userId"));
            
            User user = new User(userId, "merrickrw23@gcc.edu", "Ryan Merrick", null);
            Semester sem = new Semester(2023,'F');
            
            ScheduleKey key = new ScheduleKey(user, sem);
            
            //Gets a current schedule or makes a new one based on ScheduleKey
            Schedule sch = userSchedules.get(key);
            if (sch == null) {
                sch = new Schedule(user, sem);
                userSchedules.put(key, sch);
            }

            ctx.json(sch);
        });


        // POST: Add a section to the schedule
        app.post("/api/schedule/add/{userId}", ctx -> {
            
            //force add if over 18 credits
            boolean force = Boolean.parseBoolean(ctx.queryParam("force"));

            System.out.println("ADD Request received for user: " + ctx.pathParam("userId")); 

            try{
                int userId = Integer.parseInt(ctx.pathParam("userId")); //gets userID from API call
                Section newSection = ctx.bodyAsClass(Section.class);
                
                //manual user
                User user = new User(userId, "merrickrw23@gcc.edu", "Ryan Merrick", null);
                Semester sem = new Semester(2023,'F');

                //builds schedule key
                ScheduleKey key = new ScheduleKey(user, sem);

                // If schedule doesn't exist, exit
                Schedule sch = userSchedules.get(key);
                if (sch == null) {
                    ctx.status(404).result("Schedule not found.");
                    return;
                }
                //adds section to schedule if there is no conflict
                String result = sch.addSection(newSection);
                if (result.equals("ADD") || (result.equals("CREDIT_LIMIT")  && force)) {
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
        app.delete("/api/schedule/drop/{userId}", ctx ->{

            System.out.println("DELETE Request received for user: " + ctx.pathParam("userId"));

            try{
                int userId = Integer.parseInt(ctx.pathParam("userId"));
                Section sectionToDrop = ctx.bodyAsClass(Section.class);
                
                //manual user
                User user = new User(userId, "merrickrw23@gcc.edu", "Ryan Merrick", null);
                Semester sem = new Semester(2023,'F');

                //builds schedule key
                ScheduleKey key = new ScheduleKey(user, sem);
                
                // If schedule doesn't exist, exit
                Schedule sch = userSchedules.get(key);
                if (sch == null) {
                    ctx.status(404).result("Schedule not found.");
                    return;
                }

                //removes section from schedule
                if (sch != null && sectionToDrop != null) {
                    // Call the updated method and check the return value
                    boolean removed = sch.dropSection(sectionToDrop);
                    
                    if (removed) {
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





// // MANUAL USER SETUP
//             User user = new User(1, "merrickrw23@gcc.edu", "Ryan Merrick", null);
//             Semester sem = new Semester(2026, 'S');
//             Schedule sch = new Schedule(user, sem);

//             //Course 1: Civ Lit

//             Department d1 = new Department("HUMA");
//             Course c1 = new Course(d1, 204, "CivLit", "History or art and civilization", 3);
//             Timeslot[] t1 = new Timeslot[] {
//                 new Timeslot("09:00:00", "10:00:00", 'M'),
//                 new Timeslot("09:00:00", "10:00:00", 'W'),
//                 new Timeslot("09:00:00", "10:00:00", 'F')
//             };
//             Professor[] p1= new Professor[]{
//                 new Professor("Dr.Smith"),
//                 new Professor("Dr. Munson")
//             };
//             Semester sem1= new Semester(2026, 'S');
//             Section s1= new Section(c1,'A',t1,p1,sem1);
//             sch.addClass(s1);
//             ctx.json(sch); // Sends schedule object to React as JSON