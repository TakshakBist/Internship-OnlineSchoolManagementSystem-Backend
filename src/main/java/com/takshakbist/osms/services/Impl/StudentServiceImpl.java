package com.takshakbist.osms.services.Impl;

import com.takshakbist.osms.controllers.IMapper;
import com.takshakbist.osms.dtos.course.AddCourseDTO;
import com.takshakbist.osms.dtos.student.AddCourseInStudentDTO;
import com.takshakbist.osms.dtos.student.AddStudentDTO;
import com.takshakbist.osms.entities.Course;
import com.takshakbist.osms.entities.Student;
import com.takshakbist.osms.exceptions.*;

import com.takshakbist.osms.repositories.CourseRepository;
import com.takshakbist.osms.repositories.StudentRepository;
import com.takshakbist.osms.services.StudentService;
import com.takshakbist.osms.utility.Utility;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {
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
        Student student = studentRepository.findById(id).orElseThrow(()->StudentNotFoundException.builder().message("Student of that ID not found").build());
        if (addStudentDTO != null) {
            student.setName(addStudentDTO.getName());
            student.setAddress(addStudentDTO.getAddress());
            student.setBirthdate(addStudentDTO.getBirthdate());
        }
        return studentRepository.save(student);
    }

    @Override
    public Student getById(Long id) {
        return studentRepository.findById(id).orElseThrow(()->StudentNotFoundException.builder().message("Student of that id not found").build());
    }

    @Override
    @Transactional
    public Student enrollInCourse(Long id, AddCourseInStudentDTO addCourseInStudentDTO) {
        Student student = studentRepository.findById(id).orElseThrow(()-> StudentNotFoundException.builder().message("Student of that id not found").build());
        Set<Course> courses = addCourseInStudentDTO.getCourses().stream()
                .map(mapper::addCourseDTOToCourse)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        for (var course :courses){
            Long courseId = course.getCourseId();
            Course course1 = courseRepository.findById(courseId).orElseThrow(()->CourseNotFoundException.builder().message("Course of that Id not found").build());
            checkIfCoursesOverlapWithStudentCoursesAndThrowException(student,course1);
            checkIfCourseCapacityIsFullAndThrowException(course1);
            student.getCourses().add(course1);
        }
     return studentRepository.save(student);
    }


    private void checkIfCoursesOverlapWithStudentCoursesAndThrowException(Student student, Course course){
        if (Utility.coursesOverlap(student.getCourses(),course)){
            throw OverlappingCoursesException.builder().message("Courses are overlapping").build();
        }
    }

    private void checkIfCourseCapacityIsFullAndThrowException(Course course){
        if (Utility.isCapacityFull(course)){
            throw CourseCapacityIsFullException.builder().message("Course Capacity is full").build();
        }
    }
    @Override
    public List<Student> getAll() {
        return Optional.of(studentRepository.findAll()).orElseThrow(()->StudentNotFoundException.builder().message("Student  not found").build());
    }

    @Override
    public String delete(Long id) {
        studentRepository.findById(id).orElseThrow(()->StudentNotFoundException.builder().message("Student of that id not found").build());
        studentRepository.deleteById(id);
        return "Student of id: " + id + " deleted";
    }

    @Override
    public Long totalCoursesEnrolled(Long id) {
        return Utility.totalCoursesEnrolledByStudent(studentRepository
                .findById(id)
                .orElseThrow(()->StudentNotFoundException.builder().message("Student of that id not found").build()));
    }

    @Override
    public List<Student> filterByBirthDate(LocalDate birthDate, String basis) {
        return studentRepository.findAll().stream()
                .filter(student -> {
                    LocalDate studentBirthdate = student.getBirthdate();
                    return studentBirthdate != null &&
                            (basis.equals("before") ? studentBirthdate.isBefore(birthDate) : studentBirthdate.isAfter(birthDate));
                })
                .collect(Collectors.toList());
    }


    @Override
    public Page<Student> getWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String field) {
        return studentRepository.findAll(PageRequest.of(pageNumber,pageSize, Sort.by(field).ascending()));
    }

    @Override
    public Student unenrollCourse(Long id, AddCourseInStudentDTO addCourseInStudentDTO) {
        Student student = studentRepository.findById(id).orElseThrow(()->StudentNotFoundException.builder().message("Student of that id not found").build());
        Set<AddCourseDTO> courseList = addCourseInStudentDTO.getCourses();

        for (var course : courseList){
            Course course1 = courseRepository.findById(course.getCourseId()).orElseThrow(()-> CourseNotFoundException.builder().message("Course of that id not found").build());
            student.getCourses().remove(course1);
        }
        return studentRepository.save(student);
    }

}
