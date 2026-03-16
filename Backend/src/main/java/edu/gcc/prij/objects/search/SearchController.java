package edu.gcc.prij.objects.search;

import java.util.Collection;
import java.util.List;

import edu.gcc.prij.objects.course.Course;
import edu.gcc.prij.objects.course.CourseKey;
import edu.gcc.prij.objects.department.Department;
import edu.gcc.prij.objects.section.Section;
import edu.gcc.prij.objects.section.SectionKey;
import edu.gcc.prij.utils.Controller;
import edu.gcc.prij.utils.Repository;
import io.javalin.Javalin;

public class SearchController implements Controller {
  private Search searchEngine;

  private Repository<Section, SectionKey> sectionRepository;
  private Repository<Department, String> departmentRepository;
  private Repository<Course, CourseKey> courseRepository;

  public SearchController(
    Repository<Section, SectionKey> sectionRepository,
    Repository<Department, String> departmentRepository,
    Repository<Course, CourseKey> courseRepository
  ){
    this.searchEngine = new Search();

    this.sectionRepository = sectionRepository;
    this.departmentRepository = departmentRepository;
    this.courseRepository = courseRepository;
  }

  @Override
  public void registerRoutes(Javalin app) {
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
