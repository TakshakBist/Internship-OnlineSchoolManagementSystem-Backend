package com.takshakbist.osms.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.takshakbist.osms.dtos.classroom.AddClassroomDTO;
import com.takshakbist.osms.entities.Classroom;
import com.takshakbist.osms.services.Impl.ClassroomServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ClassroomControllerTest {

    @Mock
    private ClassroomServiceImpl classroomService;

    @Mock
    private IMapper iMapper;

    @InjectMocks
    private ClassroomController classroomController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void givenClassroom_WhenAddClassroom_shouldReturnHTTPCREATED() throws Exception {

        AddClassroomDTO inputDTO = new AddClassroomDTO();

        Classroom classroom = new Classroom();
        when(classroomService.add(any(AddClassroomDTO.class))).thenReturn(classroom);

        when(iMapper.classroomToAddClassroomDTO(any(Classroom.class))).thenReturn(inputDTO);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(classroomController).build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/classroom/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void givenClassroom_whenUpdateClassroom_shouldReturnHTTPOK() throws Exception {
        Long classroomId = 1L;
        AddClassroomDTO inputDTO = new AddClassroomDTO();

        Classroom classroom = new Classroom();
        when(classroomService.update(any(Long.class), any(AddClassroomDTO.class))).thenReturn(classroom);

        when(iMapper.classroomToAddClassroomDTO(any(Classroom.class))).thenReturn(inputDTO);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(classroomController).build();


        mockMvc.perform(MockMvcRequestBuilders.put("/api/classroom/{id}", classroomId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void givenClassroom_WhenGetByID_shouldReturnHTTPOK() throws Exception {

        Long classroomId = 1L;
        AddClassroomDTO expectedDTO = new AddClassroomDTO();

        Classroom classroom = new Classroom();
        when(classroomService.getById(any(Long.class))).thenReturn(classroom);

        when(iMapper.classroomToAddClassroomDTO(any(Classroom.class))).thenReturn(expectedDTO);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(classroomController).build();


        mockMvc.perform(MockMvcRequestBuilders.get("/api/classroom/{id}", classroomId))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void givenClassrooms_WhenGetAllClassrooms_shouldReturnHTTP200() throws Exception {
        // Given
        AddClassroomDTO expectedDTO = new AddClassroomDTO();
        Classroom classroom = new Classroom();
        List<Classroom> classroomList = new ArrayList<>();
        classroomList.add(classroom);

        when(classroomService.getAll()).thenReturn(classroomList);

        when(iMapper.classroomToAddClassroomDTO(any(Classroom.class))).thenReturn(expectedDTO);

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(classroomController).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/classroom/"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void givenClassroom_WhenDeleteClassroom_ShouldReturnHTTP200() throws Exception {

        Long classroomId = 1L;

        when(classroomService.delete(any(Long.class))).thenReturn("Deleted successfully");

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(classroomController).build();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/classroom/{id}", classroomId))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
