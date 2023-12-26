package com.takshakbist.osms.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.takshakbist.osms.dtos.TotalDTO;
import com.takshakbist.osms.dtos.course.AddCourseDTO;
import com.takshakbist.osms.dtos.teacher.AddCourseInTeacherDTO;
import com.takshakbist.osms.dtos.teacher.AddTeacherDTO;
import com.takshakbist.osms.dtos.teacher.TeacherDTO;
import com.takshakbist.osms.entities.Teacher;
import com.takshakbist.osms.services.Impl.TeacherServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

    @Mock
    private TeacherServiceImpl teacherService;

    @Mock
    private IMapper iMapper;

    @InjectMocks
    private TeacherController teacherController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private AddTeacherDTO addTeacherDTO;
    private Teacher teacher;
    private AddCourseInTeacherDTO addCourseInTeacherDTO;
    private TeacherDTO teacherDTO;
    private MockMvc mockMvc;
    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
        addTeacherDTO = AddTeacherDTO.builder().teacherId(1L).name("John").address("Ktm").build();
        teacher = Teacher.builder().teacherId(1L).name("John").address("Ktm").courses(new HashSet<>()).build();
        addCourseInTeacherDTO = AddCourseInTeacherDTO.builder().courses(new HashSet<>()).build();
        addCourseInTeacherDTO.getCourses().add(AddCourseDTO.builder().courseId(1L).name("Thermodynamics").capacity(60).build());
        teacherDTO = TeacherDTO.builder().teacherId(1L).name("John").address("Ktm").courses(new HashSet<>()).build();
    }

    @AfterEach
    void tearDown(){
        addTeacherDTO = null;
        teacher = null;
        teacherDTO = null;
        addCourseInTeacherDTO = null;
        mockMvc = null;
    }

    @Test
    public void givenTeacher_WhenAddTeacher_ShouldAddTeacherAndReturnHTTPCREATED() throws Exception{
        when(teacherService.add(any(AddTeacherDTO.class))).thenReturn(teacher);
        when(iMapper.teacherToAddTeacherDTO(any(Teacher.class))).thenReturn(addTeacherDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/teacher")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(addTeacherDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.teacherId").value(1L))
                .andDo(print());
    }

    @Test
    public void givenTeacher_WhenUpdateTeacher_ShouldUpdateTeacherAndReturnHTTPOK() throws Exception{
        when(teacherService.update(any(Long.class),any(AddTeacherDTO.class))).thenReturn(teacher);
        when(iMapper.teacherToAddTeacherDTO(any(Teacher.class))).thenReturn(addTeacherDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/teacher/{id}",1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(addTeacherDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(addTeacherDTO))
                .andDo(print());
    }

    @Test
    public void givenTeacher_WhenTeachCourse_ShouldAddCourseToTeacherListAndReturnHTTPOK() throws Exception{
        when(teacherService.teachCourse(any(Long.class),any(AddCourseInTeacherDTO.class))).thenReturn(teacher);
        when(iMapper.teacherToAddCourseInTeacherDTO(any(Teacher.class))).thenReturn(addCourseInTeacherDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/teacher/register/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addCourseInTeacherDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses[0].courseId").value(1L))
                .andExpect(jsonPath("$.courses[0].name").value("Thermodynamics"))
                .andDo(print());
    }

    @Test
    public void givenTeacher_whenGetTeacherById_shouldGetTheTeacherAndReturnHttpOK() throws Exception{
        when(teacherService.getById(1L)).thenReturn(teacher);
        when(iMapper.teacherToTeacherDTO(any(Teacher.class))).thenReturn(teacherDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/{id}",1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(teacherDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.teacherId").value(1L))
                .andDo(print());
    }

    @Test
    public void givenTeacher_whenGetAll_ShouldReturnListOfTeacherAndHttpOK() throws Exception{
        when(teacherService.getAll()).thenReturn(List.of(teacher));
        when(iMapper.teacherToAddTeacherDTO(any(Teacher.class))).thenReturn(addTeacherDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/get/{basis}","name")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(addTeacherDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].teacherId").value(1L))
                .andDo(print());
    }

    @Test
    public void givenTeacher_whenDeleteTeacher_shouldDeleteTeacherAndReturnHttpOK() throws Exception{
        when(teacherService.delete(1L)).thenReturn("Teacher of id: 1 deleted");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/teacher/{id}",1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Teacher of id: 1 deleted"))
                .andDo(print());
    }

    @Test
    public void givenTeacher_whenGetTotalEnrolledStudents_shouldReturnTotalStudentsEnrolledAndHttpOk() throws Exception {
        TotalDTO totalDTO = TotalDTO.builder().build();
        when(teacherService.getTotalStudentsEnrolled(1L)).thenReturn(10L);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/total/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(totalDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(10L))
                .andDo(print());
    }

    @Test
    public void givenStudent_whenGetWithPagination_shouldReturnPaginatedListAndHTTPOK() throws Exception {

        when(teacherService.getWithPaginationAndSorting(1,10,"fieldName")).thenReturn(new PageImpl<>(List.of(teacher)));
        when(iMapper.teacherToAddTeacherDTO(any())).thenReturn(addTeacherDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/teacher/page/{pageNumber}/{pageSize}/{field}",1,10,"fieldName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].teacherId").value(1L))
                .andDo(print());
    }
}
