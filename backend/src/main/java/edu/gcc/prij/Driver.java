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
import edu.gcc.prij.objects.semester.Semester;
import edu.gcc.prij.objects.semester.SemesterKey;
import edu.gcc.prij.objects.statussheet.StatusSheetController;
import edu.gcc.prij.objects.user.UserController;
import edu.gcc.prij.objects.user.User;
import edu.gcc.prij.utils.Controller;
import edu.gcc.prij.utils.CustomJsonParser;
import edu.gcc.prij.utils.InMemoryRepository;
import edu.gcc.prij.utils.Repository;
import edu.gcc.prij.utils.SQLiteRepository;
import io.javalin.Javalin;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.IOException;
import java.io.InputStream;

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
        Repository<Semester, SemesterKey> semesterRepository;
        Repository<User, Integer> userRepository;
        
        if(true){
            sectionRepository = new SQLiteRepository<>(Section.class);
            courseRepository = new SQLiteRepository<>(Course.class);
            departmentRepository = new SQLiteRepository<>(Department.class);
            professorRepository = new SQLiteRepository<>(Professor.class);
            ratingRepository = new SQLiteRepository<>(Rating.class);
            scheduleRepository = new SQLiteRepository<>(Schedule.class);
            semesterRepository = new SQLiteRepository<>(Semester.class);
            userRepository = new SQLiteRepository<>(User.class);
        }
        if(false){
            sectionRepository = new InMemoryRepository<>();
            courseRepository = new InMemoryRepository<>();
            departmentRepository = new InMemoryRepository<>();
            professorRepository = new InMemoryRepository<>();
            ratingRepository = new InMemoryRepository<>();
            scheduleRepository = new InMemoryRepository<>();
            semesterRepository = new InMemoryRepository<>();
            userRepository = new InMemoryRepository<>();
        }
        /* ---------- CREATE REPOSITORIES ---------- */

        /* ---------- PARSE JSON FILE ---------- */
        if(true){
            List<String> jsonFiles = List.of(
                "/courses_2025_fall.json",
                "/courses_2026_spring.json",
                "/courses_2026_fall.json",
                "/courses_2027_spring.json"
            );
            
            for (String jsonFile : jsonFiles){
                new CustomJsonParser(jsonFile, sectionRepository, departmentRepository, courseRepository, professorRepository, semesterRepository).parse();
            }
        }
        /* ---------- PARSE JSON FILE ---------- */

        /* ---------- INITIALIZE FIREBASE ---------- */
   
        try {
            // finds the firebase JSON key file
            InputStream serviceAccount = Driver.class.getClassLoader()
                    .getResourceAsStream("prijproject-firebase-adminsdk-fbsvc-03123a40d9.json");

            if (serviceAccount == null) {
                System.err.println("!! CRITICAL: Firebase JSON key not found in src/main/resources !!");
            } else {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("Firebase Admin SDK initialized successfully.");
            }
        } catch (IOException e) {
            System.err.println("Firebase Init Error: " + e.getMessage());
        }

        /* ---------- INITIALIZE FIREBASE ---------- */

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
            new ScheduleController(sectionRepository, departmentRepository, courseRepository, scheduleRepository, semesterRepository, userRepository),
            new RatingController(ratingRepository),
            new StatusSheetController()
        );

        controllers.forEach(c -> c.registerRoutes(app));
        /* ---------- INITIALIZE CONTROLLERS AND REGISTER ROUTES ---------- */
    }
}
