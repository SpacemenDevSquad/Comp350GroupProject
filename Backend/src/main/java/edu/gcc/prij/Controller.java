package edu.gcc.prij;

import io.javalin.Javalin;

public interface Controller {
    public abstract void registerRoutes(Javalin app);
}
