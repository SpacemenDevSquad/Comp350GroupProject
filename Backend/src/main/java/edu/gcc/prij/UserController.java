package edu.gcc.prij;

import java.util.Map;
import io.javalin.Javalin;

public class UserController implements Controller {
    public void registerRoutes(Javalin app) {
        app.get("/user/{userId}", ctx -> {
            ctx.json(Map.of("item", "user "+ctx.pathParam("userId")));
        });
    }
}
