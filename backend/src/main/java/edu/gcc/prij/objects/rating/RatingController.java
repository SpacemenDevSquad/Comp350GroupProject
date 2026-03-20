package edu.gcc.prij.objects.rating;

import edu.gcc.prij.utils.Controller;
import edu.gcc.prij.utils.Repository;
import io.javalin.Javalin;

public class RatingController implements Controller {
  private Repository<Rating, Integer> ratingRepository;

  public RatingController(Repository<Rating, Integer> ratingRepository){
    this.ratingRepository = ratingRepository;
  }

  @Override
  public void registerRoutes(Javalin app) {
    // TODO Auto-generated method stub
    app.get("/api/rating/{id}", ctx -> {
      int id = Integer.parseInt(ctx.pathParam("id"));
      ctx.json(ratingRepository.findById(id));
    });
  }
}
