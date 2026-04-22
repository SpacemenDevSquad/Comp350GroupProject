package edu.gcc.prij;
import edu.gcc.prij.utils.Controller;
import io.javalin.Javalin;

public class Ping implements Controller {
    public void registerRoutes(Javalin app) {
        app.get("/ping", ctx -> {
            ctx.json("{ \"Connected\": true }");
        });
    }
}
