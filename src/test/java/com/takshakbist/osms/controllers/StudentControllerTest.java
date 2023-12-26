package com.takshakbist.osms.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.takshakbist.osms.dtos.TotalDTO;
import com.takshakbist.osms.dtos.course.AddCourseDTO;
import com.takshakbist.osms.dtos.student.AddCourseInStudentDTO;
import com.takshakbist.osms.dtos.student.AddStudentDTO;
import com.takshakbist.osms.dtos.student.StudentDTO;
import com.takshakbist.osms.entities.Student;
import com.takshakbist.osms.services.Impl.StudentServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    @InjectMocks
    private StudentController studentController;

    @Mock
    private StudentServiceImpl studentService;

    @Mock
    private IMapper iMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private AddStudentDTO addStudentDTO;
    private Student student;
    private AddCourseInStudentDTO addCourseInStudentDTO;
    private Set<AddCourseDTO> addCourseDTOS;
    private StudentDTO studentDTO;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
        addStudentDTO = AddStudentDTO.builder().studentId(1L).name("Takshak").address("Tikapur").build();
        student = Student.builder().studentId(1L).name("Takshak").address("Tikapur").courses(new HashSet<>()).build();
        studentDTO = StudentDTO.builder().studentId(1L).name("Takshak").address("Tikapur").courses(new HashSet<>()).build();
        addCourseDTOS = new HashSet<>();
        addCourseDTOS.add(AddCourseDTO.builder().courseId(1L).name("Thermodynamics").capacity(60).build());
        addCourseInStudentDTO = AddCourseInStudentDTO.builder().build();
        addCourseInStudentDTO.setCourses(addCourseDTOS);
    }

    @AfterEach
    void tearDown(){
        addStudentDTO = null;
        student = null;
        addCourseInStudentDTO = null;
        addCourseDTOS = null;
        studentDTO = null;
    }
    @Test
    public void givenStudent_whenAddStudent_shouldReturnHTTPCREATED() throws Exception {

        when(studentService.add(any(AddStudentDTO.class))).thenReturn(student);
        when(iMapper.studentToAddStudentDTO(any(Student.class))).thenReturn(addStudentDTO);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/student")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(addStudentDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Takshak"))
                .andExpect(jsonPath("$.studentId").value(1L))
                .andDo(print());
    }

    @Test
    public void givenStudent_whenUpdateStudent_shouldReturnHTTPOK() throws Exception{
        when(studentService.update(eq(1L),any(AddStudentDTO.class))).thenReturn(student);
        when(iMapper.studentToAddStudentDTO(any(Student.class))).thenReturn(addStudentDTO);


        mockMvc.perform(MockMvcRequestBuilders.put("/api/student/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addStudentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Takshak"))
                .andExpect(jsonPath("$.studentId").value(1L))
                .andDo(print());
    }

    @Test
    public void givenStudent_whenEnrollStudentInCourse_shouldEnrollStudentAndReturnHTTPOK() throws Exception {
            when(studentService.enrollInCourse(eq(1L),any(AddCourseInStudentDTO.class))).thenReturn(student);
            when(iMapper.studentToAddCourseInStudentDTO(any(Student.class))).thenReturn(addCourseInStudentDTO);

            mockMvc.perform(MockMvcRequestBuilders.put("/api/student/enroll/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addCourseInStudentDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.courses[0].name").value("Thermodynamics"))
                    .andExpect(jsonPath("$.courses[0].courseId").value(1L))
                    .andDo(print());
    }

    @Test
    public void givenStudent_whenGetById_shouldGetStudentAndReturnHTTPOK() throws Exception{
        when(studentService.getById(eq(1L))).thenReturn(student);
        when(iMapper.studentToStudentDTO(any(Student.class))).thenReturn(studentDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/student/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Takshak"))
                .andExpect(jsonPath("$.studentId").value(1L))
                .andExpect(jsonPath("$.address").value("Tikapur"))
                .andDo(print());
    }

    @Test
    public void givenStudent_whenGetAll_shouldGetListOfStudentAndReturnHTTPOK() throws Exception{
        when(studentService.getAll()).thenReturn(List.of(student));
        when(iMapper.studentToStudentDTO(any())).thenReturn(studentDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/student/get/{basis}","name")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(List.of())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].studentId").value(1L))
                .andExpect(jsonPath("$[0].name").value("Takshak"))
                .andDo(print());
    }

    @Test
    public void givenStudent_whenGetTotalCoursesEnrolled_shouldGetTotalCoursesEnrolledAndReturnHTTPOK() throws Exception{
        TotalDTO totalDTO = TotalDTO.builder().build();
        when(studentService.totalCoursesEnrolled(1L)).thenReturn(10L);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/student/total/{id}",1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(totalDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(10L))
                .andDo(print());
    }

    @Test
    public void givenStudent_whenDeleteStudent_shouldDeleteStudentAndReturnHTTPOK() throws Exception{
        when(studentService.delete(1L)).thenReturn("Student of id: 1 deleted");

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/student/{id}",1L))
                .andExpect(status().isOk())
                .andExpect(content().string("Student of id: 1 deleted"))
                .andDo(print());

    }

    @Test
    public void givenStudent_whenGetWithPagination_shouldReturnPaginatedListAndHTTPOK() throws Exception {

        when(studentService.getWithPaginationAndSorting(1,10,"fieldName")).thenReturn(new PageImpl<>(List.of(student)));
        when(iMapper.studentToAddStudentDTO(any())).thenReturn(addStudentDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/student/page/{pageNumber}/{pageSize}/{field}",1,10,"fieldName")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(List.of())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Takshak"))
                .andExpect(jsonPath("$[0].studentId").value(1L))
                .andDo(print());
    }

    @Test
    public void givenStudent_whenUnenrollStudent_shouldUnenrollFromCourseAndReturnHTTPOK() throws Exception{

        addCourseInStudentDTO.setCourses(new HashSet<>());

        when(studentService.unenrollCourse(1L,addCourseInStudentDTO)).thenReturn(student);
        when(iMapper.studentToStudentDTO(any(Student.class))).thenReturn(studentDTO);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/student/unenroll/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Takshak"))
                .andExpect(jsonPath("$.studentId").value(1L))
                .andDo(print());
    }

}
