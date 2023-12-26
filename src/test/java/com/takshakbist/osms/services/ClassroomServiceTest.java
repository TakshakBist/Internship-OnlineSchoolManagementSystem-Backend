package com.takshakbist.osms.services;

import com.takshakbist.osms.controllers.IMapper;
import com.takshakbist.osms.dtos.classroom.AddClassroomDTO;
import com.takshakbist.osms.entities.Classroom;
import com.takshakbist.osms.entities.Course;
import com.takshakbist.osms.exceptions.ClassroomNotFoundException;
import com.takshakbist.osms.repositories.ClassroomRepository;
import com.takshakbist.osms.repositories.CourseRepository;
import com.takshakbist.osms.services.Impl.ClassroomServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClassroomServiceTest {
    @Mock
    private ClassroomRepository classroomRepository;

    @Mock
    private IMapper iMapper;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private ClassroomServiceImpl classroomService;
    private AddClassroomDTO classroom1;
    private Classroom classroom;
    private Classroom classroom3;
    private List<Classroom> classrooms;

    @BeforeEach
    public void setup(){
        classroom1 = AddClassroomDTO.builder().classroomId(1L).name("Room 101").build();
        classrooms = new ArrayList<>();
        HashSet<Course> courses = new HashSet<>();
        courses.add(new Course());
        classroom = Classroom.builder().classroomId(1L).name("Room 101").courses(courses).build();
        classroom3 = Classroom.builder().classroomId(1L).name("Room 102").courses(courses).build();
        classrooms.add(classroom);
        classrooms.add(classroom3);
    }

    @AfterEach
    public void tearDown(){
        classroom1 =   null;
        classroom =  classroom3 = null;
        classrooms = null;
    }

    @Test
    public void givenClassroomToAdd_ShouldReturnAddedClassroom(){
        when(classroomRepository.save(any())).thenReturn(classroom);
        when(iMapper.addClassroomDTOToCLassroom(any())).thenReturn(classroom);

        classroomService.add(classroom1);
        verify(classroomRepository,times(1)).save(any());
    }

    @Test
    public void givenClassroomToUpdate_ShouldReturnUpdatedClassroom(){
        when(classroomRepository.findById(1L)).thenReturn(Optional.ofNullable(classroom));
        when(classroomRepository.save(any())).thenReturn(classroom);

        Long id = 1L;
        classroomService.update(id, classroom1);

        verify(classroomRepository,times(1)).findById(id);
        verify(classroomRepository,times(1)).save(any());
    }

    @Test
    public void givenClassroomToUpdate_WhenNoClassroomOfGivenIdIsFound_ShouldThrowClassroomNotFoundException(){
        Long id = 1L;
        when(classroomRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ClassroomNotFoundException.class,()->classroomService.getById(id));
    }

    @Test
    public void givenClassroomToGet_WhenClassroomOfIdFound_ShouldReturnClassroom(){
        Long id = 1L;
        when(classroomRepository.findById(id)).thenReturn(Optional.ofNullable(classroom));

        Classroom classroom4 = classroomService.getById(id);
        verify(classroomRepository,times(1)).findById(id);
        assertEquals(id,classroom4.getClassroomId());

    }

    @Test
    public void givenClassroomToGet_WhenClassroomOfIdNotFound_ShouldThrowClassroomNotFoundException(){
        Long id = 1L;
        when(classroomRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ClassroomNotFoundException.class,()->classroomService.getById(id));
    }

    @Test
    public void givenListOfClassroomToGet_WhenClassroomsFound_ShouldReturnListOfClassroom(){
        when(classroomRepository.findAll()).thenReturn(classrooms);
        List<Classroom> classrooms1 = new ArrayList<>(classrooms);
        List<Classroom> classrooms2 = classroomService.getAll();
        verify(classroomRepository,times(1)).findAll();
        assertEquals(classrooms1,classrooms2);
    }

    @Test
    public void givenListOfClassroomToGet_WhenClassroomsNotFound_ShouldReturnEmptyList(){
        when(classroomRepository.findAll()).thenReturn(List.of());
        List<Classroom> classrooms1 = List.of();
        List<Classroom> classrooms2 = classroomService.getAll();
        verify(classroomRepository,times(1)).findAll();
        assertEquals(classrooms1,classrooms2);
    }


    @Test
    public void givenClassroomToDelete_WhenClassroomOfIdFound_ShouldDeleteClassroom(){
        Long id = 1L;
        Course course = classroom.getCourses().iterator().next();

        when(classroomRepository.findById(id)).thenReturn(Optional.of(classroom));
        when(courseRepository.save(any())).thenReturn(course);

        String result = classroomService.delete(id);

        verify(classroomRepository, times(1)).findById(id);
        verify(courseRepository, times(1)).save(course);
        verify(classroomRepository, times(1)).deleteById(id);

        assertEquals("Classroom of id "  + id + " deleted", result);
    }

    @Test
    public void givenClassroomToDelete_WhenClassroomOfIdNotFound_ShouldThrowClassroomNotFoundException(){
        Long id = 1L;
        when(classroomRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ClassroomNotFoundException.class,()->classroomService.delete(id));
    }


}
