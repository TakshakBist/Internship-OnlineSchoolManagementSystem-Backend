package com.takshakbist.osms.services;

import com.takshakbist.osms.controllers.IMapper;
import com.takshakbist.osms.dtos.course.AddCourseDTO;
import com.takshakbist.osms.dtos.teacher.AddCourseInTeacherDTO;
import com.takshakbist.osms.dtos.teacher.AddTeacherDTO;
import com.takshakbist.osms.entities.Course;
import com.takshakbist.osms.entities.Teacher;

import com.takshakbist.osms.exceptions.TeacherNotFoundException;
import com.takshakbist.osms.repositories.CourseRepository;
import com.takshakbist.osms.repositories.TeacherRepository;
import com.takshakbist.osms.services.Impl.TeacherServiceImpl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private IMapper mapper;

    @InjectMocks
    private TeacherServiceImpl teacherService;

    private Teacher teacher;
    private AddTeacherDTO addTeacherDTO;
    private AddCourseInTeacherDTO addCourseInTeacherDTO;
    private Set<AddCourseDTO> courses;

    @BeforeEach
    void setUp() {
        teacher = Teacher.builder().build();
        addTeacherDTO = AddTeacherDTO.builder().build();
        addCourseInTeacherDTO = AddCourseInTeacherDTO.builder().build();
        courses = new HashSet<>();
        courses.add(new AddCourseDTO());
        addCourseInTeacherDTO.setCourses(courses);
    }

    @AfterEach
    void tearDown(){
       teacher = null;
       addTeacherDTO = null;
       addCourseInTeacherDTO = null;
       courses = null;
    }

    @Test
    void givenTeacherToAdd_thenTeacherIsSavedSuccessfully() {
        when(mapper.addTeacherDTOToTeacher(addTeacherDTO)).thenReturn(teacher);
        when(teacherRepository.save(teacher)).thenReturn(teacher);

        Teacher result = teacherService.add(addTeacherDTO);

        verify(mapper, times(1)).addTeacherDTOToTeacher(addTeacherDTO);
        verify(teacherRepository, times(1)).save(teacher);
        assertEquals(teacher, result);
    }

    @Test
    void givenTeacherToUpdate_whenTeacherOfIdFound_thenTeacherIsUpdatedSuccessfully() {
        Long teacherId = 1L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));
        when(teacherRepository.save(teacher)).thenReturn(teacher);

        Teacher result = teacherService.update(teacherId, addTeacherDTO);

        verify(teacherRepository, times(1)).findById(teacherId);
        verify(teacherRepository, times(1)).save(teacher);
        assertEquals(teacher, result);
    }

    @Test
    void givenTeacherId_whenGetById_thenTeacherIsRetrievedSuccessfully() {
        Long teacherId = 1L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        Teacher result = teacherService.getById(teacherId);

        verify(teacherRepository, times(1)).findById(teacherId);
        assertEquals(teacher, result);
    }

    @Test
    void whenGetAll_thenAllTeachersAreRetrievedSuccessfully() {
        when(teacherRepository.findAll()).thenReturn(List.of(teacher));

        List<Teacher> result = teacherService.getAll();

        verify(teacherRepository, times(1)).findAll();
        assertEquals(List.of(teacher), result);
    }

    @Test
    void givenTeacherId_whenDelete_thenTeacherIsDeletedSuccessfully() {
        Long teacherId = 1L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(teacher));

        String result = teacherService.delete(teacherId);

        verify(teacherRepository, times(1)).findById(teacherId);
        verify(teacherRepository, times(1)).deleteById(teacherId);
        assertEquals("Teacher of id :1 deleted", result);
    }

    @Test
    void givenTeacherId_whenGetTotalStudentsEnrolled_WhenTeacherNotFound_thenThrowException() {
        Long teacherId = 1L;
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.empty());

        TeacherNotFoundException exception = assertThrows(TeacherNotFoundException.class, () -> {
            teacherService.getTotalStudentsEnrolled(teacherId);
        });

        assertEquals("Teacher not found", exception.getMessage());

        verify(teacherRepository, times(1)).findById(teacherId);
    }


    @Test
    void givenPageNumberPageSizeAndField_whenGetWithPaginationAndSorting_thenTeachersArePaginatedAndSortedSuccessfully() {
        int pageNumber = 1;
        int pageSize = 10;
        String field = "name";

        when(teacherRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(field).ascending())))
                .thenReturn(Page.empty());

        Page<Teacher> result = teacherService.getWithPaginationAndSorting(pageNumber, pageSize, field);

        verify(teacherRepository, times(1)).findAll(PageRequest.of(pageNumber, pageSize, Sort.by(field).ascending()));
        assertEquals(Page.empty(), result);
    }
}
