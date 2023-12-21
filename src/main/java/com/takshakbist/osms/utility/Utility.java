package com.takshakbist.osms.utility;


import com.takshakbist.osms.entities.Course;
import com.takshakbist.osms.entities.Student;
import com.takshakbist.osms.entities.Teacher;


import java.util.Objects;
import java.util.Set;

public class Utility {

    public static boolean isNull(Object object){
        return object==null;
    }
    public static boolean coursesOverlap(Set<Course> existingCourses, Course newCourse) {
        if (existingCourses.isEmpty())
            return false;

        return existingCourses.stream().anyMatch(existingCourse -> timeOverlap(existingCourse, newCourse));
    }
    private static boolean timeOverlap(Course course1, Course course2) {
        return course1.getStartTime().equals(course2.getStartTime()) ||
                (course1.getStartTime().isBefore(course2.getEndTime()) && course1.getEndTime().isAfter(course2.getStartTime())) ||
                (course2.getStartTime().isBefore(course1.getEndTime()) && course2.getEndTime().isAfter(course1.getStartTime()));
    }

    public static long totalStudentsEnrolledInTeacherCourse(Teacher teacher){
        return   teacher.getCourses().stream()
                .flatMap(course -> course.getStudents().stream())
                .distinct()
                .count();

    }

    public static long totalCoursesEnrolledByStudent(Student student){
        return student.getCourses().size();

    }

    public static boolean isCapacityFull(Course course){
        return course.getStudents().size() == course.getCapacity();
    }
}
