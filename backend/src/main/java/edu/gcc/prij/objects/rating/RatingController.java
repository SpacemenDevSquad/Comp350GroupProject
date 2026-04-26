package edu.gcc.prij.objects.rating;

import edu.gcc.prij.objects.user.User;
import edu.gcc.prij.utils.Controller;
import edu.gcc.prij.utils.Repository;
import io.javalin.Javalin;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RatingController implements Controller {
    private Repository<Rating, Integer> ratingRepository;
    private Repository<User, String> userRepository;

    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public RatingController(Repository<Rating, Integer> ratingRepository, Repository<User, String> userRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void registerRoutes(Javalin app) {

        // GET: All ratings
        app.get("/api/ratings", ctx -> {
            ctx.json(ratingRepository.findAll());
        });

        // GET: Single rating by ID
        app.get("/api/rating/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Rating rating = ratingRepository.findById(id);
            if (rating == null) {
                ctx.status(404).result("Rating not found.");
            } else {
                ctx.json(rating);
            }
        });

        // GET: All ratings by a specific user
        app.get("/api/ratings/user/{userId}", ctx -> {
            String userId = ctx.pathParam("userId");
            List<Rating> ratings = ratingRepository.findAll().stream()
                .filter(r -> r.getUser() != null && r.getUser().getId().equals(userId))
                .toList();
            ctx.json(ratings);
        });

        // GET: All ratings for a specific professor
        app.get("/api/ratings/professor/{professorName}", ctx -> {
            String professorName = ctx.pathParam("professorName");
            List<Rating> ratings = ratingRepository.findAll().stream()
                .filter(r -> r.getProfessor() != null && r.getProfessor().getName().equalsIgnoreCase(professorName))
                .toList();
            ctx.json(ratings);
        });

        // GET: All ratings for a specific course
        app.get("/api/ratings/course/{department}/{number}", ctx -> {
            String deptCode = ctx.pathParam("department");
            int number = Integer.parseInt(ctx.pathParam("number"));
            List<Rating> ratings = ratingRepository.findAll().stream()
                .filter(r -> r.getCourse() != null 
                        && r.getCourse().getDepartment() != null 
                        && r.getCourse().getDepartment().getCode().equalsIgnoreCase(deptCode)
                        && r.getCourse().getNumber() == number)
                .toList();
            ctx.json(ratings);
        });

        // GET: All ratings for a specific course AND professor combination
        app.get("/api/ratings/course/{department}/{number}/professor/{professorName}", ctx -> {
            String deptCode = ctx.pathParam("department");
            int number = Integer.parseInt(ctx.pathParam("number"));
            String professorName = ctx.pathParam("professorName");
            List<Rating> ratings = ratingRepository.findAll().stream()
                .filter(r -> r.getCourse() != null 
                        && r.getCourse().getDepartment() != null 
                        && r.getCourse().getDepartment().getCode().equalsIgnoreCase(deptCode)
                        && r.getCourse().getNumber() == number
                        && r.getProfessor() != null
                        && r.getProfessor().getName().equalsIgnoreCase(professorName))
                .toList();
            ctx.json(ratings);
        });

        // GET: Check if a specific user has already rated a course/professor combo
        app.get("/api/ratings/user/{userId}/course/{department}/{number}/professor/{professorName}", ctx -> {
            String userId = ctx.pathParam("userId");
            String deptCode = ctx.pathParam("department");
            int number = Integer.parseInt(ctx.pathParam("number"));
            String professorName = ctx.pathParam("professorName");

            Rating existing = ratingRepository.findAll().stream()
                .filter(r -> r.getUser() != null && r.getUser().getId().equals(userId))
                .filter(r -> r.getCourse() != null
                        && r.getCourse().getDepartment() != null
                        && r.getCourse().getDepartment().getCode().equalsIgnoreCase(deptCode)
                        && r.getCourse().getNumber() == number)
                .filter(r -> r.getProfessor() != null
                        && r.getProfessor().getName().equalsIgnoreCase(professorName))
                .findFirst()
                .orElse(null);

            if (existing == null) {
                ctx.status(404).result("No existing rating found.");
            } else {
                ctx.json(existing);
            }
        });

        // POST: Create a new rating (or update if user already rated this combo)
        app.post("/api/rating", ctx -> {
            try {
                Rating incoming = ctx.bodyAsClass(Rating.class);

                // Validate difficulty and quality ranges (1-3)
                if (!incoming.isValid()) {
                    ctx.status(400).result("Invalid difficulty or quality. Both must be between 1 and 3.");
                    return;
                }

                // Ensure user exists (getOrAdd creates a fallback if missing)
                User user = incoming.getUser();
                if (user == null || user.getId() == null || user.getId().isBlank()) {
                    ctx.status(400).result("User ID is required.");
                    return;
                }
                User validatedUser = userRepository.getOrAdd(user.getId(), user);
                String userId = validatedUser.getId();

                // Check if user already rated this exact course/professor combo
                String deptCode = incoming.getCourse() != null && incoming.getCourse().getDepartment() != null
                    ? incoming.getCourse().getDepartment().getCode() : "";
                int courseNum = incoming.getCourse() != null ? incoming.getCourse().getNumber() : -1;
                String profName = incoming.getProfessor() != null ? incoming.getProfessor().getName() : "";

                Rating existing = ratingRepository.findAll().stream()
                    .filter(r -> r.getUser() != null && r.getUser().getId().equals(userId))
                    .filter(r -> r.getCourse() != null
                            && r.getCourse().getDepartment() != null
                            && r.getCourse().getDepartment().getCode().equalsIgnoreCase(deptCode)
                            && r.getCourse().getNumber() == courseNum)
                    .filter(r -> r.getProfessor() != null
                            && r.getProfessor().getName().equalsIgnoreCase(profName))
                    .findFirst()
                    .orElse(null);

                if (existing != null) {
                    // Update existing rating
                    Rating updated = new Rating(
                        existing.getId(),
                        validatedUser,
                        incoming.getCourse(),
                        incoming.getProfessor(),
                        incoming.getDifficulty(),
                        incoming.getQuality(),
                        incoming.getReview()
                    );
                    ratingRepository.update(existing.getId(), updated);
                    ctx.status(200).json(updated);
                    return;
                }

                // Auto-generate ID if not provided or already exists
                int newId = incoming.getId();
                if (newId <= 0 || ratingRepository.findById(newId) != null) {
                    newId = generateUniqueId();
                }

                Rating newRating = new Rating(
                    newId,
                    validatedUser,
                    incoming.getCourse(),
                    incoming.getProfessor(),
                    incoming.getDifficulty(),
                    incoming.getQuality(),
                    incoming.getReview()
                );

                ratingRepository.save(newId, newRating);
                ctx.status(201).json(newRating);

            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500).result("Error creating rating: " + e.getMessage());
            }
        });

        // PUT: Update an existing rating
        app.put("/api/rating/{id}", ctx -> {
            try {
                int id = Integer.parseInt(ctx.pathParam("id"));
                Rating existing = ratingRepository.findById(id);
                if (existing == null) {
                    ctx.status(404).result("Rating not found.");
                    return;
                }

                Rating incoming = ctx.bodyAsClass(Rating.class);

                if (!incoming.isValid()) {
                    ctx.status(400).result("Invalid difficulty or quality. Both must be between 1 and 3.");
                    return;
                }

                // Ensure user exists
                User user = incoming.getUser();
                if (user == null || user.getId() == null || user.getId().isBlank()) {
                    ctx.status(400).result("User ID is required.");
                    return;
                }
                User validatedUser = userRepository.getOrAdd(user.getId(), user);

                Rating updated = new Rating(
                    id,
                    validatedUser,
                    incoming.getCourse(),
                    incoming.getProfessor(),
                    incoming.getDifficulty(),
                    incoming.getQuality(),
                    incoming.getReview()
                );

                ratingRepository.update(id, updated);
                ctx.status(200).json(updated);

            } catch (Exception e) {
                e.printStackTrace();
                ctx.status(500).result("Error updating rating: " + e.getMessage());
            }
        });

        // DELETE: Remove a rating
        app.delete("/api/rating/{id}", ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean deleted = ratingRepository.deleteById(id);
            if (deleted) {
                ctx.status(200).result("Rating deleted successfully.");
            } else {
                ctx.status(404).result("Rating not found.");
            }
        });
    }

    private int generateUniqueId() {
        int id;
        do {
            id = idGenerator.getAndIncrement();
        } while (ratingRepository.findById(id) != null);
        return id;
    }
}
