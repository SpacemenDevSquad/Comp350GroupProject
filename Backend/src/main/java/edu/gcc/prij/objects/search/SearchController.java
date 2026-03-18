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
    app.post("/api/search", ctx -> {
            
      // A. Catch the JSON from React and turn it into a SearchQuery object
      SearchQuery userTicket = ctx.bodyAsClass(SearchQuery.class);

      // B. Grab the master list from your awesome repository pattern
      Collection<Section> masterCatalog = sectionRepository.findAll();

      /// Harcoded filters to exclude sections with null semesters, Fall 2023 sections, and sections with "zload" in the title
      masterCatalog = masterCatalog.stream()
                    .filter(section -> section.getSemester() != null) // Filter out sections with null semesters
                    .filter(section -> (section.getSemester().getTerm() == 'F' && section.getSemester().getYear() == 2023)) // Filter out Fall 2023 sections
                    .filter(section -> !section.getCourse().getTitle().toLowerCase().contains("zload")) // Filter out sections with "zload" in the title
                    .collect(Collectors.toList());

      // C. Hand the ticket and the data to your stateless engine
      List<Section> results = searchEngine.executeSearch(userTicket, masterCatalog);

      // D. Package the exact matches back into JSON and send them to the frontend
      ctx.json(results);
    });

    app.get("/api/autocomplete", ctx -> {
    // Grab the text the user is currently typing (e.g., ?q=soft)
    String query = ctx.queryParam("q");
    
    if (query == null || query.trim().length() < 2) {
        // Return an empty list if they haven't typed much yet
        ctx.json(new ArrayList<>());
        return;
    }

    String lowerQuery = query.toLowerCase();

    // Grab your data (using the repository we saw earlier)
    Collection<Section> masterCatalog = sectionRepository.findAll();

    /// Harcoded filters to exclude sections with null semesters, Fall 2023 sections, and sections with "zload" in the title
    masterCatalog = masterCatalog.stream()
                    .filter(section -> section.getSemester() != null) // Filter out sections with null semesters
                    .filter(section -> (section.getSemester().getTerm() == 'F' && section.getSemester().getYear() == 2023)) // Filter out Fall 2023 sections
                    .filter(section -> !section.getCourse().getTitle().toLowerCase().contains("zload")) // Filter out sections with "zload" in the title
                    .collect(Collectors.toList());

    // Use a Stream to quickly filter and extract the names
    List<String> suggestions = masterCatalog.stream()
            .map(section -> section.getCourse().getTitle()) // Pluck out just the course name
            .filter(title -> title != null && title.toLowerCase().contains(lowerQuery)) // Match the text
            .distinct() // Remove duplicates
            .sorted() // Alphabetize them
            .limit(5) // Only send the top 5 matches back to React
            .collect(Collectors.toList());

    ctx.json(suggestions);
});
  }
  
}
