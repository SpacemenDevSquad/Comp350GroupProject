package edu.gcc.prij.objects.section;

import edu.gcc.prij.objects.search.Search;
import edu.gcc.prij.objects.search.SearchQuery;
import edu.gcc.prij.objects.course.Course;
import edu.gcc.prij.objects.course.CourseKey;
import edu.gcc.prij.objects.department.Department;
import edu.gcc.prij.objects.semester.Semester;
import edu.gcc.prij.utils.Controller;
import edu.gcc.prij.utils.Repository;
import io.javalin.Javalin;

import java.util.Collection;
import java.util.List;

public class SectionController implements Controller {
    private Repository<Section, SectionKey> sectionRepository;
    private Repository<Department, String> departmentRepository;
    private Repository<Course, CourseKey> courseRepository;

    private Search searchEngine;

    public SectionController(
        Repository<Section, SectionKey> sectionRepository,
        Repository<Department, String> departmentRepository,
        Repository<Course, CourseKey> courseRepository
    ){
        this.sectionRepository = sectionRepository;
        this.departmentRepository = departmentRepository;
        this.courseRepository = courseRepository;

        this.searchEngine = new Search();
    }

    @Override
    public void registerRoutes(Javalin app) {
        app.get("/api/section/{year}/{term}/{department}/{number}/{section}", ctx -> {
            int year = Integer.parseInt(ctx.pathParam("year"));
            char term = ctx.pathParam("term").charAt(0);
            // Semester semester = Semester.addOrGet(year, term);
            Semester semester = new Semester(year, term);

            Department department = departmentRepository.findById(ctx.pathParam("department"));

            int number = Integer.parseInt(ctx.pathParam("number"));
            CourseKey courseKey = new CourseKey(department, number);
            Course course = courseRepository.findById(courseKey);

            char sectionLetter = ctx.pathParam("section").charAt(0);

            SectionKey sectionKey = new SectionKey(course, sectionLetter, semester);
            Section fallbackSection = new Section(course, sectionLetter, null, null, semester);

            ctx.json(sectionRepository.getOrAdd(sectionKey, fallbackSection));
        });

        app.get("/api/sections", ctx -> {
            ctx.json(sectionRepository.findAll());
        });

        app.post("/api/search", ctx -> {
            
            // A. Catch the JSON from React and turn it into a SearchQuery object
            SearchQuery userTicket = ctx.bodyAsClass(SearchQuery.class);

            // B. Grab the master list from your awesome repository pattern
            Collection<Section> masterCatalog = sectionRepository.findAll();

            // C. Hand the ticket and the data to your stateless engine
            List<Section> results = searchEngine.executeSearch(userTicket, masterCatalog);

            // D. Package the exact matches back into JSON and send them to the frontend
            ctx.json(results);
        });
    }
    
}
