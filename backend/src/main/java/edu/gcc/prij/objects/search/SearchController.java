package edu.gcc.prij.objects.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    app.post("/api/search/{year}/{term}", ctx -> {
            
      // Catch the JSON from React and turn it into a SearchQuery object
      SearchQuery userTicket = ctx.bodyAsClass(SearchQuery.class);

      // Get the complete list of sections from the repository
      Collection<Section> masterCatalog = sectionRepository.findAll();

      /// Harcoded filters to exclude sections with null semesters, Fall 2023 sections, and sections with "zload" as the department
      masterCatalog = masterCatalog.stream()
                    .filter(section -> section.getSemester() != null) // Filter out sections with null semesters
                    .filter(section -> (section.getSemester().getTerm() == ctx.pathParam("term").charAt(0) && section.getSemester().getYear() == Integer.parseInt(ctx.pathParam("year"))))
                    .filter(section -> !section.getCourse().getDepartment().getCode().toLowerCase().contains("zload")) // Filter out sections with dept = "zload"
                    .collect(Collectors.toList());

      // Pass the search query and the complete section list to the search engine
      List<Section> results = searchEngine.executeSearch(userTicket, masterCatalog);

      // Put the section results into a JSON and send them to the frontend
      ctx.json(results);
    });

    app.get("/api/autocomplete/{year}/{term}", ctx -> {
    // Grab the text the user is currently typing
    String query = ctx.queryParam("q");
    
    if (query == null || query.trim().length() < 2) {
        // Return an empty list if they haven't typed much
        ctx.json(new ArrayList<>());
        return;
    }
    String lowerQuery = query.toLowerCase();
    // Get all the sections 
    Collection<Section> masterCatalog = sectionRepository.findAll();

    /// Harcoded filters to exclude sections with null semesters, Fall 2023 sections, and sections with "zload" as the department
    masterCatalog = masterCatalog.stream()
                    .filter(section -> section.getSemester() != null) // Filter out sections with null semesters
                    .filter(section -> (section.getSemester().getTerm() == ctx.pathParam("term").charAt(0) && section.getSemester().getYear() == Integer.parseInt(ctx.pathParam("year"))))
                    .filter(section -> !section.getCourse().getDepartment().getCode().toLowerCase().contains("zload")) // Filter out sections with "zload" as the department
                    .collect(Collectors.toList());

    // Filter and extract the names
    List<String> suggestions = masterCatalog.stream()
            .map(section -> section.getCourse().getTitle()) // Get just the course names
            .filter(title -> title != null && title.toLowerCase().contains(lowerQuery)) // Match the text
            .distinct() // Remove duplicates
            .sorted() // Sort alphabetically
            .limit(5) // Only send the top 5 matches back to the frontend
            .collect(Collectors.toList());

    ctx.json(suggestions);
});
  }
  
}
