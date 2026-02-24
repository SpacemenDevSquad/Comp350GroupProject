package edu.gcc.prij;

import java.util.List;

import io.javalin.Javalin;

/**
 * @author YOHOJR23
 * @author Ryan Merrick
 * @author Isaiah Zimmerman
 * @author Peter Brumbach
*/

public class Driver {
    public static void main(String[] args) {
        System.out.println("Hello jars");
        System.out.println("jsdfklifbr3bvcoqjksdj");
        System.out.println("test");

        Timeslot ts = new Timeslot(480, 530, 'M');
        System.out.println(ts);

        // uncomment after first exercise, index.html needs to be in the resources/public new folder
        Javalin app = Javalin.create(config -> {
            // Serve static files from: src/main/resources/public
            // config.staticFiles.add("public");
        }).start(7000);

        List<Controller> controllers = List.of(
            new CourseController(),
            new UserController(),
            new SectionController(),
            new ScheduleController()
        );

        controllers.forEach(c -> c.registerRoutes(app));

        CustomJsonParser.parseCustomData();
//        Timeslot ts = new Timeslot(480, 530, 'M');
//        System.out.println(ts);
    }
}
