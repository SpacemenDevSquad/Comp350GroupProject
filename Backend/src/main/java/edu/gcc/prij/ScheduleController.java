package edu.gcc.prij;

import java.util.ArrayList;
import java.util.Map;

import io.javalin.Javalin;

public class ScheduleController implements Controller {
    public void registerRoutes(Javalin app){
        // MANUAL TEST SCHEDULE

        //Course 1: Civ Lit
        Course c = new Course(Department.addOrGet("HUMA"), 202, "Civ Lit", "Study Ancient and Modern Literature in this exciting class.", 3);
        Timeslot[] ts = {
            new Timeslot(120, 180, 'M'),
            new Timeslot(120, 180, 'W')
        };
        Professor[] fs = {
            new Professor("Dr. McCray"),
        };
        Semester sem = new Semester(2026, 'F');
        Section s = new Section(c, 'B', ts, fs, sem);

        // Course 2: Automata Theory
        Course c2 = new Course(Department.addOrGet("COMP"), 314, "Automata Theory", "Learn about FSMs.", 3);
        Timeslot[] ts2 = {
            new Timeslot(200, 260, 'M'),
            new Timeslot(200, 260, 'W')
        };
        Professor[] fs2 = {
            new Professor("Prof. Johnson"),
        };
        Semester sem2 = new Semester(2026, 'S');
        Section s2 = new Section(c2, 'A', ts2, fs2, sem2);

        // User and Schedule Hardcoded Variables
        ArrayList<Section> sections = new ArrayList<>();
        User u = new User(1, "test@gcc.edu", "Test Testson", null);

        Schedule sch = new Schedule(sections, u);

        sch.addClass(s);
        sch.addClass(s2);


        app.get("/schedule/{scheduleId}", ctx -> {
            ctx.json(sch);
        });

        app.post("/schedule/add", ctx -> {
            String inputJSON= ctx.body();

            Section newSection= ctx.bodyAsClass(Section.class); //converts JSON input to a class using javalin

            boolean success= sch.addClass(newSection);

            ctx.json(success);

        });

    }
}
