package edu.gcc.prij;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.Reader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class DataLoader {

    /**
     * Reads the JSON file and parses it into a List of Section objects.
     * * @param filePath The absolute or relative path to your JSON file.
     * @return A List of Sections, or an empty list if an error occurs.
     */
    public List<Section> loadCourseData(String filePath) {
        Gson gson = new Gson();

        // The try-with-resources block ensures the file reader closes automatically
        try (Reader reader = new FileReader(filePath)) {

            // This is the magic line where Gson maps the JSON text to your Java objects
            CourseCatalog catalog = gson.fromJson(reader, CourseCatalog.class);

            // Safety check to ensure we actually got data back
            if (catalog != null && catalog.getClasses() != null) {
                return catalog.getClasses();
            } else {
                System.err.println("Warning: Parsed JSON was empty or did not contain 'classes'.");
                return new ArrayList<>(); // Return empty list to prevent crashes later
            }

        } catch (IOException e) {
            // If the file isn't found or can't be read, this catches the error
            System.err.println("Error reading the JSON file at " + filePath);
            e.printStackTrace();

            // Return an empty list so the app doesn't crash completely
            return new ArrayList<>();
        } catch (com.google.gson.JsonSyntaxException e) {
            // This catches errors if the JSON is formatted incorrectly
            System.err.println("Error parsing the JSON data. Check the file format.");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}