package com.takshakbist.osms.controllers;

import com.takshakbist.osms.dtos.DateTimeDTO;
import com.takshakbist.osms.dtos.TotalDTO;
import com.takshakbist.osms.dtos.teacher.AddCourseInTeacherDTO;
import com.takshakbist.osms.dtos.teacher.AddTeacherDTO;
import com.takshakbist.osms.dtos.teacher.TeacherDTO;
import com.takshakbist.osms.entities.Teacher;
import com.takshakbist.osms.services.Impl.TeacherServiceImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TeacherController {

    @NonNull
    private final TeacherServiceImpl teacherService;

    @NonNull
    private final IMapper iMapper;

    @PostMapping("/teacher")
    public ResponseEntity<AddTeacherDTO> add(@RequestBody AddTeacherDTO addTeacherDTO){
        Teacher teacher = teacherService.add(addTeacherDTO);
        return new ResponseEntity<>(iMapper.teacherToAddTeacherDTO(teacher), HttpStatus.OK);
    }

    @PutMapping("/teacher/{id}")
    public ResponseEntity<AddTeacherDTO> update(@PathVariable(name = "id") Long id, @RequestBody AddTeacherDTO addTeacherDTO){
        Teacher teacher = teacherService.update(id,addTeacherDTO);
        return new ResponseEntity<>(iMapper.teacherToAddTeacherDTO(teacher),HttpStatus.OK);
    }

    @PutMapping("/teacher/register/{id}")
    public ResponseEntity<AddCourseInTeacherDTO> teachCourse(@PathVariable(name = "id") Long id, @RequestBody AddCourseInTeacherDTO addCourseInTeacherDTO){
        Teacher teacher = teacherService.teachCourse(id,addCourseInTeacherDTO);
        return new ResponseEntity<>(iMapper.teacherToAddCourseInTeacherDTO(teacher),HttpStatus.OK);
    }

    @GetMapping("/teacher/{id}")
    public ResponseEntity<TeacherDTO> getById(@PathVariable(name = "id") Long id){
        return new ResponseEntity<>(iMapper.teacherToTeacherDTO(teacherService.getById(id)),HttpStatus.OK);
    }

    @GetMapping("/teacher")
    public ResponseEntity<List<AddTeacherDTO>> getAll(@RequestParam(name = "basis", defaultValue = "none") String basis){
        List<Teacher> teachers = teacherService.getAll();
        Stream<Teacher> teacherStream = teachers.stream();

        switch (basis){
            case "name":
                teacherStream = teacherStream.sorted(Comparator.comparing(Teacher::getName));
                break;

            case "joinDate":
                teacherStream = teacherStream.sorted(Comparator.comparing(Teacher::getJoinDate));
                break;

            default:
                break;
        }

        List<AddTeacherDTO> addTeacherDTOS = teacherStream.map(iMapper::teacherToAddTeacherDTO).collect(Collectors.toList());
        return new ResponseEntity<>(addTeacherDTOS,HttpStatus.OK);
    }

    @DeleteMapping("/teacher/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id){
        return new ResponseEntity<>(teacherService.delete(id),HttpStatus.OK);
    }

    @GetMapping("teacher/total/{id}")
    public ResponseEntity<TotalDTO> totalEnrolledStudents(@PathVariable(name = "id") Long id){
        TotalDTO totalDTO = new TotalDTO(teacherService.getTotalStudentsEnrolled(id));
        return new ResponseEntity<>(totalDTO,HttpStatus.OK);
    }

    @GetMapping("teacher/filter")
    public ResponseEntity<List<AddTeacherDTO>> filterByJoinDate(@RequestBody DateTimeDTO dateTimeDTO, @RequestParam(name = "basis") String basis){
        return new ResponseEntity<>(
                teacherService.filterByJoinDate(dateTimeDTO.getJoinDate(), basis)
                        .stream()
                        .map(iMapper::teacherToAddTeacherDTO)
                        .collect(Collectors.toList()),HttpStatus.OK
        );
    }

}
