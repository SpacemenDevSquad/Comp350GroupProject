package edu.gcc.prij.objects.search;

import java.util.Collection;
import java.util.List;

import edu.gcc.prij.objects.section.Section;
import edu.gcc.prij.objects.section.SectionKey;
import edu.gcc.prij.utils.Controller;
import edu.gcc.prij.utils.Repository;
import io.javalin.Javalin;

public class SearchController implements Controller {
  private Search searchEngine;

  private Repository<Section, SectionKey> sectionRepository;

  public SearchController(
    Repository<Section, SectionKey> sectionRepository
  ){
    this.searchEngine = new Search();
    this.sectionRepository = sectionRepository;
  }

  @Override
  public void registerRoutes(Javalin app) {
    app.post("/api/search/{year}/{term}", ctx -> {
            
      // Catch the JSON from React and turn it into a SearchQuery object
      SearchQuery userTicket = ctx.bodyAsClass(SearchQuery.class);

      // Get the complete list of sections from the repository
      Collection<Section> masterCatalog = sectionRepository.findAll();

      // Get the year and term from the path parameters
      int year = Integer.parseInt(ctx.pathParam("year"));
      char term = ctx.pathParam("term").charAt(0);

      // Filter out classes that aren't in the specified semester, and also filter out ZLOAD classes
      masterCatalog = searchEngine.getFilteredCatalog(masterCatalog, year, term);

      // Pass the search query and the complete section list to the search engine
      List<Section> results = searchEngine.executeSearch(userTicket, masterCatalog);

      // Put the section results into a JSON and send them to the frontend
      ctx.json(results);
    });

    app.get("/api/autocomplete/{year}/{term}", ctx -> {
    // Grab the text the user is currently typing
    String query = ctx.queryParam("q");

    // Get all the sections 
    Collection<Section> masterCatalog = sectionRepository.findAll();

    // Get the year and term from the path parameters
    int year = Integer.parseInt(ctx.pathParam("year"));
    char term = ctx.pathParam("term").charAt(0);

    List<String> suggestions = searchEngine.getAutocompleteSuggestions(query, masterCatalog, year, term);

    ctx.json(suggestions);
});
  }
  
}
