package edu.gcc.prij;

import java.util.List;

import edu.gcc.prij.objects.course.Course;
import edu.gcc.prij.objects.course.CourseController;
import edu.gcc.prij.objects.course.CourseKey;
import edu.gcc.prij.objects.department.Department;
import edu.gcc.prij.objects.professor.Professor;
import edu.gcc.prij.objects.rating.Rating;
import edu.gcc.prij.objects.rating.RatingController;
import edu.gcc.prij.objects.schedule.Schedule;
import edu.gcc.prij.objects.schedule.ScheduleController;
import edu.gcc.prij.objects.schedule.ScheduleKey;
import edu.gcc.prij.objects.search.SearchController;
import edu.gcc.prij.objects.section.Section;
import edu.gcc.prij.objects.section.SectionController;
import edu.gcc.prij.objects.section.SectionKey;
import edu.gcc.prij.objects.user.UserController;
import edu.gcc.prij.utils.Controller;
import edu.gcc.prij.utils.CustomJsonParser;
import edu.gcc.prij.utils.InMemoryRepository;
import edu.gcc.prij.utils.Repository;
import edu.gcc.prij.utils.SQLiteRepository;
import io.javalin.Javalin;

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

        Repository<Section, SectionKey> sectionRepository;
        Repository<Course, CourseKey> courseRepository;
        Repository<Department, String> departmentRepository;
        Repository<Professor, String> professorRepository;
        Repository<Rating, Integer> ratingRepository;
        Repository<Schedule, ScheduleKey> scheduleRepository;
        
        if(true){
            sectionRepository = new SQLiteRepository<>(Section.class);
            courseRepository = new SQLiteRepository<>(Course.class);
            departmentRepository = new SQLiteRepository<>(Department.class);
            professorRepository = new SQLiteRepository<>(Professor.class);
            ratingRepository = new SQLiteRepository<>(Rating.class);
            scheduleRepository = new SQLiteRepository<>(Schedule.class);
        }
        if(false){
            sectionRepository = new InMemoryRepository<>();
            courseRepository = new InMemoryRepository<>();
            departmentRepository = new InMemoryRepository<>();
            professorRepository = new InMemoryRepository<>();
            ratingRepository = new InMemoryRepository<>();
            scheduleRepository = new InMemoryRepository<>();
        }
        /* ---------- CREATE REPOSITORIES ---------- */

        /* ---------- PARSE JSON FILE ---------- */
        CustomJsonParser customJsonParser = new CustomJsonParser(sectionRepository, departmentRepository, courseRepository, professorRepository);
        customJsonParser.parse();
        /* ---------- PARSE JSON FILE ---------- */

        /* ---------- CREATE JAVALIN APP AND ALLOW FRONTEND ACCESS ---------- */
        Javalin app = Javalin.create(config -> {
            //Frontend access allowed
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.anyHost();
                });
            });
        }).start(8096);
        /* ---------- CREATE JAVALIN APP AND ALLOW FRONTEND ACCESS ---------- */

        /* ---------- INITIALIZE CONTROLLERS AND REGISTER ROUTES ---------- */
        List<Controller> controllers = List.of(
            new CourseController(courseRepository, departmentRepository),
            new UserController(),
            new SectionController(sectionRepository, departmentRepository, courseRepository),
            new SearchController(sectionRepository, departmentRepository, courseRepository),
            new ScheduleController(sectionRepository, departmentRepository, courseRepository, scheduleRepository),
            new RatingController(ratingRepository)
        );

        controllers.forEach(c -> c.registerRoutes(app));
        /* ---------- INITIALIZE CONTROLLERS AND REGISTER ROUTES ---------- */
    }
}
