package edu.gcc.prij.objects.user;

import java.util.Map;

import edu.gcc.prij.utils.Controller;
import edu.gcc.prij.utils.Repository;
import io.javalin.Javalin;


public class UserController implements Controller {

    private Repository<User, String> userRepository;

    // CONSTRUCTORS
    public UserController(Repository<User, String> userRepository) {
        this.userRepository = userRepository;
    }

    public void registerRoutes(Javalin app) {
        // GET: Retreive user info
        app.get("/api/user/{userId}", ctx -> {
            ctx.json(Map.of("item", "user "+ctx.pathParam("userId")));
        });
        
        // POST: Save or update user info
        app.post("/api/user/sync", ctx -> {
            User incomingUser = ctx.bodyAsClass(User.class);
            userRepository.save(incomingUser.getId(), incomingUser);
            ctx.status(200);
        });
    }
}
