package com.takshakbist.osms.services;

import com.takshakbist.osms.dtos.course.AddCourseDTO;
import com.takshakbist.osms.dtos.course.AddCourseInClassroomDTO;
import com.takshakbist.osms.entities.Course;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CourseService {
    Course add(AddCourseDTO addCourseDTO);
    Course update(Long id, AddCourseDTO addCourseDTO);
    Course addInClassroom(Long id,AddCourseInClassroomDTO addCourseInClassroomDTO);
    Course getById(Long id);
    List<Course> getAll();
    String delete(Long id);
    List<Course> filterByStartDate(LocalDate startDate, String basis);
    List<Course> filterByStartTime(LocalTime startTime, String basis);
    List<Course> filterByEndTime(LocalTime endTime, String basis);
}
