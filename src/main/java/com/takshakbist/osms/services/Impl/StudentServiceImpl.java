package com.takshakbist.osms.services.Impl;

import com.takshakbist.osms.controllers.IMapper;
import com.takshakbist.osms.dtos.student.AddCourseInStudentDTO;
import com.takshakbist.osms.dtos.student.AddStudentDTO;
import com.takshakbist.osms.entities.Course;
import com.takshakbist.osms.entities.Student;
import com.takshakbist.osms.exceptions.CourseNotFoundException;

import com.takshakbist.osms.exceptions.OverlappingCoursesException;
import com.takshakbist.osms.exceptions.StudentNotFoundException;
import com.takshakbist.osms.repositories.CourseRepository;
import com.takshakbist.osms.repositories.StudentRepository;
import com.takshakbist.osms.utility.Utility;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements com.takshakbist.osms.services.StudentService {
    @NonNull
    private final StudentRepository studentRepository;
    @NonNull
    private final IMapper mapper;
    @NonNull
    private final CourseRepository courseRepository;

    @Override
    public Student add(AddStudentDTO addStudentDTO) {
        return studentRepository.save(mapper.addStudentDTOToStudent(addStudentDTO));
    }

    @Override
    public Student update(Long id, AddStudentDTO addStudentDTO) {
        Student student = studentRepository.findById(id).orElseThrow(()->new StudentNotFoundException(""));
        if (addStudentDTO != null) {
            student.setName(addStudentDTO.getName());
            student.setAddress(addStudentDTO.getAddress());
            student.setBirthdate(addStudentDTO.getBirthdate());
        }
        return studentRepository.save(student);
    }

    @Override
    public Student getById(Long id) {
        return studentRepository.findById(id).orElseThrow(()->new StudentNotFoundException(""));
    }

    @Override
    @Transactional
    public Student enrollInCourse(Long id, AddCourseInStudentDTO addCourseInStudentDTO) {
        Student student = studentRepository.findById(id).orElseThrow(()-> new StudentNotFoundException(""));
        Set<Course> courses = addCourseInStudentDTO.getCourses().stream().map(mapper::addCourseDTOToCourse).collect(Collectors.toSet());
        for (var course :courses){
            Long courseId = course.getCourseId();
            Course course1 = courseRepository.findById(courseId).orElseThrow(()->new CourseNotFoundException(""));

            if (Utility.coursesOverlap(student.getCourses(),course1)){
                throw new OverlappingCoursesException("Courses are overlapping");
            }
            if (Utility.isCapacityFull(course1)){
                throw new CourseNotFoundException("Course Capacity is full, cannot enroll");
            }
            student.getCourses().add(course1);

        }
     return studentRepository.save(student);
    }


    @Override
    public List<Student> getAll() {
        return Optional.of(studentRepository.findAll()).orElseThrow(()->new StudentNotFoundException("Students not found"));
    }

    @Override
    public String delete(Long id) {
        studentRepository.findById(id).orElseThrow(()->new StudentNotFoundException("Id not found,Cannot delete"));
        studentRepository.deleteById(id);
        return "Student of id: " + id + " deleted";
    }

    @Override
    public Long totalCoursesEnrolled(Long id) {
        return Utility.totalCoursesEnrolledByStudent(studentRepository
                .findById(id)
                .orElseThrow(()->new StudentNotFoundException("totalCoursesEnrolled: Student of that id was not found")));
    }

    @Override
    public List<Student> filterByBirthDate(LocalDate birthDate, String basis) {
        return studentRepository.findAll().stream()
                .filter(student -> basis.equals("before") ? student.getBirthdate().isBefore(birthDate) : student.getBirthdate().isAfter(birthDate))
                .collect(Collectors.toList());
    }
}
