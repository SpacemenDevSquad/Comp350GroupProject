package edu.gcc.prij.objects.course;

import edu.gcc.prij.objects.department.Department;
import edu.gcc.prij.utils.Controller;
import edu.gcc.prij.utils.Repository;
import io.javalin.Javalin;

public class CourseController implements Controller {
    private Repository<Course, CourseKey> courseRepository;
    private Repository<Department, String> departmentRepository;

    public CourseController(Repository<Course, CourseKey> courseRepository, Repository<Department, String> departmentRepository){
        this.courseRepository = courseRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void registerRoutes(Javalin app) {
        app.get("/api/course/{department}/{number}", ctx -> {
            Department department = departmentRepository.findById(ctx.pathParam("department"));
            CourseKey courseKey = new CourseKey(department, Integer.parseInt(ctx.pathParam("number")));
            ctx.json(courseRepository.findById(courseKey));
        }); 
    }
}
