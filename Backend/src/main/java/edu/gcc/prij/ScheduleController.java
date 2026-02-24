package edu.gcc.prij;

import java.util.ArrayList;
import java.util.Map;

import io.javalin.Javalin;

public class ScheduleController implements Controller {
    public void registerRoutes(Javalin app){
        Course c = new Course(Department.addOrGet("HUMA"), 202, "Civ Lit", "Study Ancient and Modern Literature in this exciting class.", 3);

        Timeslot[] ts = {
            new Timeslot(120, 180, 'M'),
            new Timeslot(120, 180, 'W')
        };

        Professor[] fs = {
            new Professor(1, "Dr. McCray"),
        };

        Semester sem = new Semester(2026, 'F');

        Section s = new Section(c, 'B', ts, fs, sem);

        Course c2 = new Course(Department.addOrGet("COMP"), 314, "Automata Theory", "Learn about FSMs.", 3);

        Timeslot[] ts2 = {
            new Timeslot(200, 260, 'M'),
            new Timeslot(200, 260, 'W')
        };

        Professor[] fs2 = {
            new Professor(2, "Prof. Johnson"),
        };

        Semester sem2 = new Semester(2026, 'S');

        Section s2 = new Section(c2, 'A', ts2, fs2, sem2);

        ArrayList<Section> sections = new ArrayList<Section>();
        sections.add(s);
        sections.add(s2);

        User u = new User(1, "test@gcc.edu", "Test Testson", null);

        Schedule sch = new Schedule(sections, u);

        app.get("/schedule/{scheduleId}", ctx -> {
            ctx.json(sch);
        });
    }
}
