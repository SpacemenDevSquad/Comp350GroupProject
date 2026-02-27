package edu.gcc.prij.utils;

import io.javalin.Javalin;

public interface Controller {
    public abstract void registerRoutes(Javalin app);
}
