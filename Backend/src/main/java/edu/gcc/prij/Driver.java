package edu.gcc.prij;

import java.util.List;

import edu.gcc.prij.objects.course.Course;
import edu.gcc.prij.objects.course.CourseController;
import edu.gcc.prij.objects.course.CourseKey;
import edu.gcc.prij.objects.department.Department;
import edu.gcc.prij.objects.professor.Professor;
import edu.gcc.prij.objects.rating.Rating;
import edu.gcc.prij.objects.rating.RatingController;
import edu.gcc.prij.objects.schedule.ScheduleController;
import edu.gcc.prij.objects.section.Section;
import edu.gcc.prij.objects.section.SectionController;
import edu.gcc.prij.objects.section.SectionKey;
import edu.gcc.prij.objects.user.UserController;
import edu.gcc.prij.utils.Controller;
import edu.gcc.prij.utils.CustomJsonParser;
import edu.gcc.prij.utils.InMemoryRepository;
import edu.gcc.prij.utils.Repository;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

/**
 * @author James Yoho
 * @author Ryan Merrick
 * @author Isaiah Zimmerman
 * @author Peter Brumbach
*/

public class Driver {
    public static void main(String[] args) {
        /* ---------- CREATE REPOSITORIES ---------- */
        // Repository<Type, KeyType> name = new InMemoryRepository<>();
        Repository<Section, SectionKey> sectionRepository = new InMemoryRepository<>();
        Repository<Course, CourseKey> courseRepository = new InMemoryRepository<>();
        Repository<Department, String> departmentRepository = new InMemoryRepository<>();
        Repository<Professor, String> professorRepository = new InMemoryRepository<>();
        Repository<Rating, Integer> ratingRepository = new InMemoryRepository<>();
        /* ---------- CREATE REPOSITORIES ---------- */

        /* ---------- PARSE JSON FILE ---------- */
        CustomJsonParser customJsonParser = new CustomJsonParser(sectionRepository, departmentRepository, courseRepository, professorRepository);
        customJsonParser.parse();
        /* ---------- PARSE JSON FILE ---------- */

        /* ---------- CREATE JAVALIN APP AND ADD STATIC FILES ---------- */
        Javalin app = Javalin.create(config -> {
            // Serve static files from: /Frontend/dist (react build path)
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "Frontend/dist";
                staticFiles.location = Location.EXTERNAL;
            });;
        }).start(8096);
        /* ---------- CREATE JAVALIN APP AND ADD STATIC FILES ---------- */

        /* ---------- INITIALIZE CONTROLLERS AND REGISTER ROUTES ---------- */
        List<Controller> controllers = List.of(
            new CourseController(courseRepository, departmentRepository),
            new UserController(),
            new SectionController(sectionRepository, departmentRepository, courseRepository),
            new ScheduleController(),
            new RatingController(ratingRepository)
        );

        controllers.forEach(c -> c.registerRoutes(app));
        /* ---------- INITIALIZE CONTROLLERS AND REGISTER ROUTES ---------- */

        Rating r = new Rating(1, null, null, null, 0, 0, null);
        ratingRepository.getOrAdd(1, r);
    }
}
