package edu.gcc.prij.objects.statussheet;

import edu.gcc.prij.utils.Controller;
import io.javalin.Javalin;
import io.javalin.http.Context;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class StatusSheetController implements Controller {

    public StatusSheetController() {
    }

    @Override
    public void registerRoutes(Javalin app) {
        app.get("/api/status-sheet/{major}", this::getStatusSheet);
    }

    private void getStatusSheet(Context ctx) {
        String major = ctx.pathParam("major");

        try {
            // 1. Look for the file in your src/main/resources folder
            InputStream is = getClass().getResourceAsStream("/" + major + ".json");
            
            // 2. Safety check in case the file name is wrong
            if (is == null) {
                ctx.status(404).result("{\"error\": \"Could not find " + major + ".json in resources\"}");
                return;
            }

            // 3. Read the entire file into a String (Java 9+ makes this beautifully simple)
            String jsonContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);

            // 4. Send it straight to React!
            ctx.contentType("application/json");
            ctx.result(jsonContent);

        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("{\"error\": \"Server crashed while reading the file\"}");
        }
    }
}