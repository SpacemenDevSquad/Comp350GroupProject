package edu.gcc.prij;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import edu.gcc.prij.objects.course.Course;
import edu.gcc.prij.objects.department.Department;
import edu.gcc.prij.objects.schedule.Schedule;
import edu.gcc.prij.objects.section.Section;
import edu.gcc.prij.objects.semester.Semester;
import edu.gcc.prij.objects.user.User;
import edu.gcc.prij.utils.time.Timeslot;

class ScheduleTest {
    private Section makeSection(Department dept, int number, String title, char letter, int startMin, int endMin) {
        Course course = new Course(dept, number, title, "", 3);
        Semester semester = new Semester(2026, 'F');
        Timeslot[] timeslots = new Timeslot[] { new Timeslot(startMin, endMin, 'M') };
        return new Section(course, letter, timeslots, null, semester);
    }

    @Test
    void allowsLectureAndLabForSameCourseCode() {
        Department comp = new Department("COMP");
        Schedule schedule = new Schedule(new User("u1", "u1@gcc.edu", "User One"), new Semester(2026, 'F'), "Main");

        Section lecture = makeSection(comp, 350, "SOFTWARE ENGINEERING", 'A', 540, 590);
        Section lab = makeSection(comp, 350, "SOFTWARE ENGINEERING LAB", 'L', 600, 650);

        assertEquals("ADD", schedule.addSection(lecture, false));
        assertEquals("ADD", schedule.addSection(lab, false));
    }

    @Test
    void blocksSecondLectureForSameCourseCode() {
        Department comp = new Department("COMP");
        Schedule schedule = new Schedule(new User("u2", "u2@gcc.edu", "User Two"), new Semester(2026, 'F'), "Main");

        Section lectureA = makeSection(comp, 350, "SOFTWARE ENGINEERING", 'A', 540, 590);
        Section lectureB = makeSection(comp, 350, "SOFTWARE ENGINEERING", 'B', 600, 650);

        assertEquals("ADD", schedule.addSection(lectureA, false));
        assertEquals("DUPLICATE", schedule.addSection(lectureB, false));
    }

    @Test
    void blocksSecondLabForSameCourseCode() {
        Department comp = new Department("COMP");
        Schedule schedule = new Schedule(new User("u3", "u3@gcc.edu", "User Three"), new Semester(2026, 'F'), "Main");

        Section labA = makeSection(comp, 350, "SOFTWARE ENGINEERING LAB", 'L', 540, 590);
        Section labB = makeSection(comp, 350, "SOFTWARE ENGINEERING LAB", 'M', 600, 650);

        assertEquals("ADD", schedule.addSection(labA, false));
        assertEquals("DUPLICATE", schedule.addSection(labB, false));
    }
}
