package com.takshakbist.osms.services;

import com.takshakbist.osms.controllers.IMapper;
import com.takshakbist.osms.dtos.course.AddCourseDTO;
import com.takshakbist.osms.dtos.student.AddCourseInStudentDTO;
import com.takshakbist.osms.dtos.student.AddStudentDTO;
import com.takshakbist.osms.entities.Course;
import com.takshakbist.osms.entities.Student;
import com.takshakbist.osms.exceptions.StudentNotFoundException;
import com.takshakbist.osms.repositories.CourseRepository;
import com.takshakbist.osms.repositories.StudentRepository;
import com.takshakbist.osms.services.Impl.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private IMapper mapper;


    @InjectMocks
    private StudentServiceImpl studentService;

    private Student student1;
    private AddStudentDTO addStudentDTO;
    private AddCourseInStudentDTO addCourseInStudentDTO;

    @BeforeEach
    public void setup() {
        student1 = new Student();
        addStudentDTO = new AddStudentDTO();
        addCourseInStudentDTO = new AddCourseInStudentDTO();

    }

    @AfterEach
    public void tearDown() {
        student1 = null;
        addStudentDTO = null;
        addCourseInStudentDTO = null;

    }

    @Test
    public void givenAddStudent_ShouldReturnAddedStudent() {
        when(studentRepository.save(any())).thenReturn(student1);
        when(mapper.addStudentDTOToStudent(addStudentDTO)).thenReturn(student1);

        Student result = studentService.add(addStudentDTO);

        verify(studentRepository, times(1)).save(any());
        verify(mapper, times(1)).addStudentDTOToStudent(addStudentDTO);
        assertEquals(student1, result);
    }

    @Test
    public void givenUpdateStudent_WhenStudentExists_ShouldReturnUpdatedStudent() {
        Long id = 1L;
        when(studentRepository.findById(id)).thenReturn(Optional.of(student1));
        when(studentRepository.save(student1)).thenReturn(student1);

        Student result = studentService.update(id, addStudentDTO);

        verify(studentRepository, times(1)).findById(id);
        verify(studentRepository, times(1)).save(student1);
        assertEquals(student1, result);
    }

    @Test
    public void givenUpdateStudent_WhenStudentDoesNotExist_ShouldThrowStudentNotFoundException() {
        Long id = 1L;
        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.update(id, addStudentDTO));

        verify(studentRepository, times(1)).findById(id);
        verify(studentRepository, never()).save(any());
    }


    @Test
    public void givenEnrollInCourse_WhenStudentAndCoursesExist_ShouldReturnUpdatedStudent() {
        Long studentId = 1L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student1));
        when(studentRepository.save(student1)).thenReturn(student1);

        Student result = studentService.enrollInCourse(studentId, addCourseInStudentDTO);

        verify(studentRepository, times(1)).findById(studentId);
        verify(studentRepository, times(1)).save(student1);
        assertEquals(student1, result);
    }


    @Test
    public void givenDeleteStudent_WhenStudentExists_ShouldReturnDeletedMessage() {
        Long id = 1L;
        when(studentRepository.findById(id)).thenReturn(Optional.of(student1));

        String result = studentService.delete(id);

        verify(studentRepository, times(1)).findById(id);
        verify(studentRepository, times(1)).deleteById(id);
        assertEquals("Student of id: 1 deleted", result);
    }

    @Test
    public void givenDeleteStudent_WhenStudentDoesNotExist_ShouldThrowStudentNotFoundException() {
        Long id = 1L;
        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> studentService.delete(id));

        verify(studentRepository, times(1)).findById(id);
        verify(studentRepository, never()).deleteById(id);
    }

    @Test
    public void givenTotalCoursesEnrolled_WhenStudentExists_ShouldReturnTotalCourses() {
        Long id = 1L;
        when(studentRepository.findById(id)).thenReturn(Optional.of(student1));
        when(studentRepository.findById(2L)).thenReturn(Optional.of(new Student()));

        Long result = studentService.totalCoursesEnrolled(id);
        assertEquals(0L, result);

        result = studentService.totalCoursesEnrolled(2L);
        assertEquals(0L, result);

        verify(studentRepository, times(2)).findById(anyLong());
    }


    @Test
    public void givenGetWithPaginationAndSorting_ShouldReturnPageOfStudents() {
        Integer pageNumber = 1;
        Integer pageSize = 10;
        String field = "fieldName";
        when(studentRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(field).ascending())))
                .thenReturn(new PageImpl<>(Collections.singletonList(student1)));

        Page<Student> result = studentService.getWithPaginationAndSorting(pageNumber, pageSize, field);

        verify(studentRepository, times(1)).findAll(PageRequest.of(pageNumber, pageSize, Sort.by(field).ascending()));
        assertEquals(Collections.singletonList(student1), result.getContent());
    }
}
