package com.takshakbist.osms.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.takshakbist.osms.dtos.DateTimeDTO;
import com.takshakbist.osms.dtos.course.AddCourseDTO;
import com.takshakbist.osms.dtos.course.AddCourseInClassroomDTO;
import com.takshakbist.osms.entities.Course;
import com.takshakbist.osms.services.Impl.CourseServiceImpl;
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

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    @Mock
    private CourseServiceImpl courseService;

    @Mock
    private IMapper mapper;

    @InjectMocks
    private CourseController courseController;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;
    private AddCourseDTO expectedDTO;

    private AddCourseDTO inputDTO ;

    private Course course;

    @BeforeEach
    public void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
        expectedDTO = AddCourseDTO.builder().build();
        inputDTO = AddCourseDTO.builder().build();
        course = Course.builder().build();
    }

    @AfterEach
    public void tearDown(){
        mockMvc = null;
        expectedDTO = null;
        inputDTO = null;
        course = null;
    }

    @Test
    void givenCourse_WhenAddCourse_ShouldReturnHTTPCREATED() throws Exception {


        when(courseService.add(any(AddCourseDTO.class))).thenReturn(course);
        when(mapper.courseToAddCourseDTO(any(Course.class))).thenReturn(inputDTO);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void givenCourse_WhenUpdateCourse_ShouldReturnHTTPOK() throws Exception {

        Long courseId = 1L;
        when(courseService.update(any(Long.class), any(AddCourseDTO.class))).thenReturn(course);

        when(mapper.courseToAddCourseDTO(any(Course.class))).thenReturn(inputDTO);


        mockMvc.perform(MockMvcRequestBuilders.put("/api/course/{id}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void givenCourse_WhenAddCourseInClassroom_ShouldReturnHTTPOK() throws Exception {
        Long classroomId = 1L;
        AddCourseInClassroomDTO inputDTO = AddCourseInClassroomDTO.builder().build();


        when(courseService.addInClassroom(any(Long.class), any(AddCourseInClassroomDTO.class))).thenReturn(course);

        when(mapper.courseToAddCourseInClassroomDTO(any(Course.class))).thenReturn(inputDTO);



        mockMvc.perform(MockMvcRequestBuilders.put("/api/course/add/{id}", classroomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void givenId_WhenGetCourseById_ShouldReturnHTTPOK() throws Exception {

        Long courseId = 1L;


        when(courseService.getById(any(Long.class))).thenReturn(course);

        when(mapper.courseToAddCourseDTO(any(Course.class))).thenReturn(expectedDTO);



        mockMvc.perform(MockMvcRequestBuilders.get("/api/course/{id}", courseId))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void givenCourses_WhenGetAllCourses_ShouldReturnHTTP200() throws Exception {


        when(courseService.getAll(any(String.class))).thenReturn(Collections.singletonList(course));

        when(mapper.courseToAddCourseDTO(any(Course.class))).thenReturn(expectedDTO);


        mockMvc.perform(MockMvcRequestBuilders.get("/api/course/get/{field}", "fieldName"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void givenCourses_WhenFilterCourseByStartDate_ShouldReturnHTTPOK() throws Exception {
        DateTimeDTO dateTimeDTO = DateTimeDTO.builder().build();
        String field = "startDate";
        String basis = "basis";

        List<AddCourseDTO> expectedDTOs = List.of(expectedDTO);

        when(courseService.filterByStartDate(any(), any(String.class))).thenReturn(List.of(course));

        when(mapper.courseToAddCourseDTO(any(Course.class))).thenReturn(expectedDTOs.get(0));



        mockMvc.perform(MockMvcRequestBuilders.get("/api/course/filter/{field}/{basis}", field, basis)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dateTimeDTO)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void givenCourse_WhenDeleteCourse_ShouldReturnHTTPOK() throws Exception {

        Long courseId = 1L;

        when(courseService.delete(any(Long.class))).thenReturn("Deleted successfully");


        mockMvc.perform(MockMvcRequestBuilders.delete("/api/course/{id}", courseId))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void givenCourses_WhenGetCoursesWithPagination_ShouldReturnHTTPOK() throws Exception {

        int pageNumber = 1;
        int pageSize = 10;
        String field = "fieldName";



        when(courseService.getWithPaginationAndSorting(any(int.class), any(int.class), any(String.class)))
                .thenReturn(new PageImpl<>(List.of(course)));

        when(mapper.courseToAddCourseDTO(any(Course.class))).thenReturn(expectedDTO);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/course/page/{pageNumber}/{pageSize}/{field}",
                        pageNumber, pageSize, field))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
