package edu.gcc.prij.objects.schedule;

import edu.gcc.prij.objects.semester.Semester;
import edu.gcc.prij.utils.Controller;
import io.javalin.Javalin;

public class ScheduleController implements Controller {
    public void registerRoutes(Javalin app){
        app.get("/api/schedule/{scheduleId}", ctx -> {
            // MANUAL TEST SCHEDULE
            Semester sem = new Semester(2025, 'S');

            //Course 1: Civ Lit
            // Section s = Section.get(
            //     Course.get("HIST", 390),
            //     'A',
            //     sem
            // );
            // System.out.println(s);

            // Course 2: Programming 1
            // Section s2 = Section.get(
            //     Course.get("COMP", 144),
            //     'A',
            //     sem
            // );

            // ctx.json(List.of(s));

            // User and Schedule Hardcoded Variables
            // ArrayList<Section> sections = <ArrayList> List.of(s, s2);
            // User u = new User(1, "test@gcc.edu", "Test Testson", null);

            // Schedule sch = new Schedule(sections, u);

            // sch.addClass(s);
            // sch.addClass(s2);
            
            // ctx.json(sch);
        });

        app.post("/api/schedule/add", ctx -> {
            // String inputJSON= ctx.body();

            // Section newSection= ctx.bodyAsClass(Section.class); //converts JSON input to a class using javalin

            // boolean success= sch.addClass(newSection);

            // ctx.json(success);

        });

    }
}
