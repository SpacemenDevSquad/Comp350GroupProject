package edu.gcc.prij.objects.user;

import java.util.Map;

import edu.gcc.prij.utils.Controller;
import io.javalin.Javalin;

public class UserController implements Controller {
    public void registerRoutes(Javalin app) {
        app.get("/api/user/{userId}", ctx -> {
            ctx.json(Map.of("item", "user "+ctx.pathParam("userId")));
        });
    }
}
