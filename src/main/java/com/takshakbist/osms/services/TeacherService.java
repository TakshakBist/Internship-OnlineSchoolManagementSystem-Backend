package com.takshakbist.osms.services;

import com.takshakbist.osms.dtos.teacher.AddCourseInTeacherDTO;
import com.takshakbist.osms.dtos.teacher.AddTeacherDTO;
import com.takshakbist.osms.entities.Teacher;

import java.time.LocalDate;

import java.util.List;

public interface TeacherService {
    Teacher add(AddTeacherDTO addTeacherDTO);
    Teacher update(Long id, AddTeacherDTO addTeacherDTO);
    Teacher teachCourse( Long id, AddCourseInTeacherDTO addCourseInTeacherDTO);
    Teacher getById(Long id);
    List<Teacher> getAll();
    String delete(Long id);
    Long getTotalStudentsEnrolled(Long id);
    List<Teacher> filterByJoinDate(LocalDate joinDate, String basis);
}
