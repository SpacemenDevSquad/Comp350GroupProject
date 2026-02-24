package edu.gcc.prij;

import java.util.Map;

import io.javalin.Javalin;

public class CourseController implements Controller {

    @Override
    public void registerRoutes(Javalin app) {
        app.get("/course/{department}/{number}", ctx -> {
            ctx.json(Course.get(ctx.pathParam("department"), Integer.parseInt(ctx.pathParam("number"))));
        }); 
    }
}
