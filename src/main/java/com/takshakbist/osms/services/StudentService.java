package com.takshakbist.osms.services;

import com.takshakbist.osms.dtos.student.AddCourseInStudentDTO;
import com.takshakbist.osms.dtos.student.AddStudentDTO;
import com.takshakbist.osms.entities.Student;

import java.time.LocalDate;
import java.util.List;

public interface StudentService {
    Student add(AddStudentDTO addStudentDTO);
    Student update(Long id, AddStudentDTO addStudentDTO);
    Student getById(Long id);
    Student enrollInCourse(Long id, AddCourseInStudentDTO addCourseInStudentDTO);
    List<Student> getAll();
    String delete(Long id);
    Long totalCoursesEnrolled(Long id);
    List<Student> filterByBirthDate(LocalDate birthDate, String basis);

}
