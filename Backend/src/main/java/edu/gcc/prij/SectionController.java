package edu.gcc.prij;

import java.util.ArrayList;

import io.javalin.Javalin;

public class SectionController implements Controller {
    @Override
    public void registerRoutes(Javalin app) {
        Course c = new Course(Department.addOrGet("HUMA"), 202, "Civ Lit", "Study Ancient and Modern Literature in this exciting class.", 3);

        Timeslot[] ts = {
            new Timeslot(120, 180, 'M'),
            new Timeslot(120, 180, 'W')
        };

        Professor[] fs = {
            new Professor("Dr. McCray"),
        };

        Semester sem = new Semester(2026, 'F');

        Section s = new Section(c, 'A', ts, fs, sem);

        app.get("/section/{year}/{term}/{department}/{number}/{section}", ctx -> {
            ctx.json(s);
        });

        app.get("/sections", ctx -> {
            ctx.json(Section.getSections());
        });
    }
    
}
