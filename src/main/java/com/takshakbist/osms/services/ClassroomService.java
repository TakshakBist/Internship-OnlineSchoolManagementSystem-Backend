package com.takshakbist.osms.services;

import com.takshakbist.osms.dtos.classroom.AddClassroomDTO;
import com.takshakbist.osms.entities.Classroom;

import java.util.List;

public interface ClassroomService {
    Classroom add(AddClassroomDTO addClassroomDTO);
    Classroom update(Long id, AddClassroomDTO addClassroomDTO);
    Classroom getById(Long id);
    List<Classroom> getAll();
    String delete(Long id);
}
