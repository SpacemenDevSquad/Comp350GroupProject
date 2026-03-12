package edu.gcc.prij.objects.schedule;
import edu.gcc.prij.objects.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gcc.prij.objects.course.Course;
import edu.gcc.prij.objects.course.CourseKey;
import edu.gcc.prij.objects.section.Section;
import edu.gcc.prij.objects.section.SectionKey;
import edu.gcc.prij.objects.semester.Semester;
import edu.gcc.prij.objects.department.Department;
import edu.gcc.prij.objects.professor.Professor;
import edu.gcc.prij.utils.Controller;
import edu.gcc.prij.utils.Repository;
import edu.gcc.prij.utils.time.Timeslot;

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
            if (sch.addSection(newSection) == true) {
                ctx.status(201).json(sch);
            } else {
                ctx.status(409).result("Conflict detected!");
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