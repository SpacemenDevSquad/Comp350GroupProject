package edu.gcc.prij;

import java.util.List;

import edu.gcc.prij.objects.course.Course;
import edu.gcc.prij.objects.course.CourseController;
import edu.gcc.prij.objects.course.CourseKey;
import edu.gcc.prij.objects.department.Department;
import edu.gcc.prij.objects.professor.Professor;
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
        // Repository<Type, KeyType> name = new InMemoryRepository<>();
        Repository<Section, SectionKey> sectionRepository = new InMemoryRepository<>();
        Repository<Course, CourseKey> courseRepository = new InMemoryRepository<>();
        Repository<Department, String> departmentRepository = new InMemoryRepository<>();
        Repository<Professor, String> professorRepository = new InMemoryRepository<>();

        CustomJsonParser customJsonParser = new CustomJsonParser(sectionRepository, departmentRepository, courseRepository, professorRepository);
        customJsonParser.parse();

        Javalin app = Javalin.create(config -> {
            // Serve static files from: src/main/resources/public
            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/";
                staticFiles.directory = "Frontend/dist";
                staticFiles.location = Location.EXTERNAL;
            });;
        }).start(8096);

        List<Controller> controllers = List.of(
            new CourseController(courseRepository, departmentRepository),
            new UserController(),
            new SectionController(sectionRepository, departmentRepository, courseRepository),
            new ScheduleController()
        );

        controllers.forEach(c -> c.registerRoutes(app));
    }
}
