package com.takshakbist.osms.controllers;

import com.takshakbist.osms.dtos.classroom.AddClassroomDTO;
import com.takshakbist.osms.entities.Classroom;
import com.takshakbist.osms.services.Impl.ClassroomServiceImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/classroom")
public class ClassroomController {

    @NonNull
    private final ClassroomServiceImpl classroomService;
    @NonNull
    private final IMapper iMapper;

    @PostMapping("/")
    public ResponseEntity<AddClassroomDTO> add(@RequestBody AddClassroomDTO addClassroomDTO){
        Classroom classroom = classroomService.add(addClassroomDTO);
        return new ResponseEntity<>(iMapper.classroomToAddClassroomDTO(classroom), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddClassroomDTO> update(@PathVariable(name = "id") Long id, @RequestBody AddClassroomDTO addClassroomDTO){
        Classroom classroom = classroomService.update(id,addClassroomDTO);
        return new ResponseEntity<>(iMapper.classroomToAddClassroomDTO(classroom),HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddClassroomDTO> getById(@PathVariable(name = "id")Long id){
        Classroom classroom = classroomService.getById(id);
        return new ResponseEntity<>(iMapper.classroomToAddClassroomDTO(classroom),HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<List<AddClassroomDTO>> getAll(){
        return new ResponseEntity<>(classroomService.getAll().stream().map(iMapper::classroomToAddClassroomDTO).collect(Collectors.toList()),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id){
        return new ResponseEntity<>(classroomService.delete(id),HttpStatus.OK);
    }
}
