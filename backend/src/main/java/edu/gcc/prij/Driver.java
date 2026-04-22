package edu.gcc.prij;

import java.util.Comparator;
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
import edu.gcc.prij.utils.RMPParser;
import edu.gcc.prij.utils.Repository;
import edu.gcc.prij.utils.SQLiteRepository;
import io.javalin.Javalin;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * @author James Yoho
 * @author Ryan Merrick
 * @author Isaiah Zimmerman
 * @author Peter Brumbach
*/

@Command(name = "CourseSchedulingApp", mixinStandardHelpOptions = true)
public class Driver implements Runnable {
    @Option(names = "--read-courses", description = "Enable reading courses")
    private boolean readCourses = true;

    @Option(names = "--read-rmp", description = "Enable reading RMP")
    private boolean readRmp = true;

    // String flag with a default value
    @Option(names = "--db-type", description = "Type of database to use")
    private String dbType = "sqlite";

    @Option(names = "--clear-db", description = "Clear the database before starting")
    private boolean clearDb = false;

    public static void main(String[] args) {
        // Picocli automatically parses the args and populates the variables
        new CommandLine(new Driver()).execute(args);
    }

    @Override
    public void run() {
        /* ---------- CREATE REPOSITORIES ---------- */
        Repository<Section, SectionKey> sectionRepository;
        Repository<Course, CourseKey> courseRepository;
        Repository<Department, String> departmentRepository;
        Repository<Professor, String> professorRepository;
        Repository<Rating, Integer> ratingRepository;
        Repository<Schedule, ScheduleKey> scheduleRepository;
        Repository<Semester, SemesterKey> semesterRepository;
        Repository<User, String> userRepository;
        
        if(dbType.equals("sqlite")){
            if(clearDb) {
                deleteDatabaseFolder("sqlite");
            }

            sectionRepository = new SQLiteRepository<>(Section.class);
            courseRepository = new SQLiteRepository<>(Course.class);
            departmentRepository = new SQLiteRepository<>(Department.class);
            professorRepository = new SQLiteRepository<>(Professor.class);
            ratingRepository = new SQLiteRepository<>(Rating.class);
            scheduleRepository = new SQLiteRepository<>(Schedule.class);
            semesterRepository = new SQLiteRepository<>(Semester.class);
            userRepository = new SQLiteRepository<>(User.class);
        }
        else if(dbType.equals("inmemory")){
            sectionRepository = new InMemoryRepository<>();
            courseRepository = new InMemoryRepository<>();
            departmentRepository = new InMemoryRepository<>();
            professorRepository = new InMemoryRepository<>();
            ratingRepository = new InMemoryRepository<>();
            scheduleRepository = new InMemoryRepository<>();
            semesterRepository = new InMemoryRepository<>();
            userRepository = new InMemoryRepository<>();
        }
        else {
            System.err.println("Invalid Storage Type!");
            return;
        }
        /* ---------- CREATE REPOSITORIES ---------- */

        /* ---------- PARSE COURSE JSON FILES ---------- */
        if(readCourses){
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
        /* ---------- PARSE COURSE JSON FILES ---------- */

        /* ---------- PARSE RMP JSON FILE ---------- */
        if(readRmp){
            new RMPParser("/professors_clean.json", professorRepository).parse();
            
            // After RMP data is loaded, update all sections with fresh professor data from repository
            System.out.println("\n--- Updating sections with fresh professor data from RMP ---");
            for (Section section : sectionRepository.findAll()) {
                if (section.getFaculty() != null && section.getFaculty().length > 0) {
                    Professor[] updatedFaculty = new Professor[section.getFaculty().length];
                    for (int i = 0; i < section.getFaculty().length; i++) {
                        String profName = section.getFaculty()[i].getName();
                        // Get the fresh professor from repository (which now has RMP data)
                        Professor freshProfessor = professorRepository.findById(profName);
                        updatedFaculty[i] = (freshProfessor != null) ? freshProfessor : section.getFaculty()[i];
                    }
                    section.setFaculty(updatedFaculty);
                    sectionRepository.save(section.getKey(), section);
                }
            }
            System.out.println("--- Section update complete ---\n");
        }
        /* ---------- PARSE RMP JSON FILE ---------- */

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
            new UserController(userRepository),
            new SectionController(sectionRepository, departmentRepository, courseRepository),
            new SearchController(sectionRepository, departmentRepository, courseRepository),
            new ScheduleController(sectionRepository, departmentRepository, courseRepository, scheduleRepository, semesterRepository, userRepository),
            new RatingController(ratingRepository),
            new StatusSheetController(),
            new Ping()
        );

        controllers.forEach(c -> c.registerRoutes(app));
        /* ---------- INITIALIZE CONTROLLERS AND REGISTER ROUTES ---------- */
    }

    private void deleteDatabaseFolder(String folderPath) {
        Path path = Paths.get(folderPath);
        if (Files.exists(path)) {
            try {
                // Walk the folder, sort in reverse (files first, then directories), and delete
                Files.walk(path)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
                System.out.println("Successfully wiped old SQLite folder: " + folderPath);
            } catch (IOException e) {
                System.err.println("Failed to delete SQLite folder: " + e.getMessage());
            }
        }
    }
}