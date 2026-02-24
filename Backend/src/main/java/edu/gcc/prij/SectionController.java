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
            new Professor(1, "Dr. McCray"),
        };

        Semester sem = new Semester(2026, 'F');

        Section s = new Section(c, 'A', ts, fs, sem);

        app.get("/section/{year}/{term}/{department}/{number}/{section}", ctx -> {
            int year = Integer.parseInt(ctx.pathParam("year"));
            char term = ctx.pathParam("term").charAt(0);
            Semester semester = Semester.addOrGet(year, term);

            Department department = Department.get(ctx.pathParam("department"));
            int number = Integer.parseInt(ctx.pathParam("number"));
            Course course = Course.get(department, number);

            char sectionLetter = ctx.pathParam("section").charAt(0);

            Section section = Section.addOrGet(course, sectionLetter, semester);

            ctx.json(section);
        });
    }
    
}
