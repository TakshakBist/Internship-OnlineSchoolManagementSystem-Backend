package com.takshakbist.osms.services;

import com.takshakbist.osms.controllers.IMapper;
import com.takshakbist.osms.dtos.classroom.AddClassroomDTO;
import com.takshakbist.osms.dtos.course.AddCourseDTO;
import com.takshakbist.osms.dtos.course.AddCourseInClassroomDTO;
import com.takshakbist.osms.entities.Classroom;
import com.takshakbist.osms.entities.Course;
import com.takshakbist.osms.exceptions.ClassroomNotFoundException;
import com.takshakbist.osms.exceptions.CourseNotFoundException;
import com.takshakbist.osms.repositories.ClassroomRepository;
import com.takshakbist.osms.repositories.CourseRepository;
import com.takshakbist.osms.services.Impl.CourseServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;

    @Mock
    private IMapper iMapper;

    @Mock
    private ClassroomRepository classroomRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course course1;
    private Course course2;
    private AddCourseDTO addCourseDTO;
    private List<Course> courses;

    @BeforeEach
    public void setup(){
         course1 = new Course();
         course2 = new Course();
         addCourseDTO = new AddCourseDTO();
         courses = new ArrayList<>();
         courses.add(course1);
         courses.add(course2);
    }

    @AfterEach
    public void tearDown(){
       course1 = course2 = null;
       addCourseDTO = null;
       courses = null;
    }

    @Test
    public void givenCourseToAdd_ShouldReturnAddedCourse(){
        when(courseRepository.save(any())).thenReturn(course1);
        when(iMapper.addCourseDTOToCourse(addCourseDTO)).thenReturn(course1);

        Course course = courseService.add(addCourseDTO);

        verify(courseRepository,times(1)).save(any());
        verify(iMapper,times(1)).addCourseDTOToCourse(addCourseDTO);
        assertEquals(course1,course);
    }

    @Test
    public void givenCourseToUpdate_WhenCourseOfThatIdFound_ShouldReturnThatCourse(){
        Long id = 1L;
        when(courseRepository.findById(id)).thenReturn(Optional.of(course1));
        when(courseRepository.save(course1)).thenReturn(course1);

        Course course = courseService.update(id,addCourseDTO);

        verify(courseRepository,times(1)).findById(id);
        verify(courseRepository,times(1)).save(course1);
        assertEquals(course1,course);
    }

    @Test
    public void givenCourseToUpdate_WhenCourseOfThatIdIsNotFound_ShouldThrowCourseNotFoundException(){
        Long id = 1L;
        when(courseRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(CourseNotFoundException.class,()->courseService.update(id,addCourseDTO));
    }

    @Test
    public void givenCourseToAddInClassroom_WhenCourseOfThatIdFound_And_ClassroomOfThatIdIsFound_ShouldAddCourseToClassroom(){

        Long courseId = 1L;
        Long classroomId = 1L;
        AddCourseInClassroomDTO addCourseInClassroomDTO = new AddCourseInClassroomDTO();
        addCourseInClassroomDTO.setClassroom(new AddClassroomDTO(classroomId, "Classroom 1"));

        Classroom classroom = new Classroom();
        classroom.setCourses(new HashSet<>());

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course1));
        when(classroomRepository.findById(classroomId)).thenReturn(Optional.of(classroom));
        when(courseRepository.save(any(Course.class))).thenAnswer(i -> i.getArguments()[0]);


        Course result = courseService.addInClassroom(courseId, addCourseInClassroomDTO);


        verify(courseRepository, times(1)).findById(courseId);
        verify(classroomRepository, times(1)).findById(classroomId);
        verify(courseRepository, times(1)).save(any(Course.class));
        assertEquals(course1, result);
        assertTrue(result.getClassroom().getCourses().contains(course1));
    }
    @Test
    public void givenCourseToAddInClassroom_WhenCourseOfThatIDNotFound_ShouldThrowCourseNotFoundException(){
        Long id = 1L;
        when(courseRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(CourseNotFoundException.class,()->courseService.addInClassroom(id,new AddCourseInClassroomDTO()));
    }

    @Test
    public void givenCourseToGetById_WhenCourseOfThatIdExists_ShouldReturnCourse() {
        Long id = 1L;
        when(courseRepository.findById(id)).thenReturn(Optional.of(course1));

        Course result = courseService.getById(id);

        verify(courseRepository, times(1)).findById(id);
        assertEquals(course1, result);
    }
    @Test
    public void givenCourseToGetById_WhenCourseOfThatIdDoesNotExist_shouldThrowCourseNotFoundException() {
        Long id = 1L;
        when(courseRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(CourseNotFoundException.class, () -> courseService.getById(id));
    }

    @Test
    public void givenListOfClassroomToGet_ShouldReturnListOfCourses() {
        String field = "fieldName";
        when(courseRepository.findAll(Sort.by(field))).thenReturn(courses);

        List<Course> result = courseService.getAll(field);

        verify(courseRepository, times(1)).findAll(Sort.by(field));
        assertEquals(courses, result);
    }


    @Test
    public void givenListOfCoursesToFilterByEndTimeToGet_ShouldReturnFilteredListOfCourses() {
        LocalTime endTime = LocalTime.now();
        String basis = "before";
        when(courseRepository.findAll()).thenReturn(courses);

        List<Course> result = courseService.filterByEndTime(endTime, basis);

        verify(courseRepository, times(1)).findAll();
        assertNotNull(result);
    }

    @Test
    public void givenListOfCourseToGetWithPagination_ShouldReturnListOfCourse() {
        Integer pageNumber = 1;
        Integer pageSize = 10;
        String field = "fieldName";
        when(courseRepository.findAll(PageRequest.of(pageNumber, pageSize, Sort.by(field).ascending())))
                .thenReturn(new PageImpl<>(courses));

        Page<Course> result = courseService.getWithPaginationAndSorting(pageNumber, pageSize, field);

        verify(courseRepository, times(1)).findAll(PageRequest.of(pageNumber, pageSize, Sort.by(field).ascending()));
        assertEquals(courses, result.getContent());
    }

    @Test
    public void givenCourseToDelete_WhenCourseExists_ShouldDeleteAndReturnMessage() {
        Long id = 1L;

        when(courseRepository.findById(id)).thenReturn(Optional.of(course1));
        doNothing().when(courseRepository).deleteById(id);
        String result = courseService.delete(id);

        verify(courseRepository, times(1)).findById(id);
        verify(courseRepository, times(1)).deleteById(id);

        assertEquals("Course of id 1 deleted", result);
    }

    @Test
    public void givenCourseToDelete_WhenCourseDoesNotExist_ShouldThrowCourseNotFoundException() {
        Long id = 1L;

        when(courseRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(CourseNotFoundException.class, () -> courseService.delete(id));

    }

}
