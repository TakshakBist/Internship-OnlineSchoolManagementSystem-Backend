package com.takshakbist.osms.controllers;

import com.takshakbist.osms.dtos.DateTimeDTO;
import com.takshakbist.osms.dtos.TotalDTO;
import com.takshakbist.osms.dtos.student.AddCourseInStudentDTO;
import com.takshakbist.osms.dtos.student.AddStudentDTO;
import com.takshakbist.osms.dtos.student.StudentDTO;
import com.takshakbist.osms.entities.Student;
import com.takshakbist.osms.services.StudentService;
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
public class StudentController {

    @NonNull
    private final StudentService studentService;
    @NonNull
    private final IMapper mapper;
    @NonNull
    private final IMapper iMapper;

    @PostMapping("/student")
    public ResponseEntity<AddStudentDTO> add(@RequestBody AddStudentDTO addStudentDTO){
        Student student = studentService.add(addStudentDTO);
        return new ResponseEntity<>(mapper.studentToAddStudentDTO(student), HttpStatus.CREATED);
    }

    @PutMapping("/student/{id}")
    public ResponseEntity<AddStudentDTO> update(@PathVariable Long id,@RequestBody AddStudentDTO addStudentDTO){
        Student student = studentService.update(id,addStudentDTO);
        return new ResponseEntity<>(mapper.studentToAddStudentDTO(student),HttpStatus.OK);
    }

    @PutMapping("/student/enroll/{id}")
    public ResponseEntity<AddCourseInStudentDTO> enroll(@PathVariable Long id, @RequestBody AddCourseInStudentDTO addCourseInStudentDTO){
        Student student = studentService.enrollInCourse(id,addCourseInStudentDTO);
        return new ResponseEntity<>(mapper.studentToAddCourseInStudentDTO(student),HttpStatus.OK);
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<StudentDTO> getById(@PathVariable(name = "id") Long id) {
        Student student = studentService.getById(id);
        return new ResponseEntity<>(mapper.studentToStudentDTO(student),HttpStatus.OK);
    }

    @GetMapping("/student/get/{basis}")
    public ResponseEntity<List<StudentDTO>> getAll(@PathVariable(name = "basis") String basis){

        List<Student> students = studentService.getAll();
        Stream<Student> studentStream = students.stream();

        switch (basis) {
            case "name":
                studentStream = studentStream.sorted(Comparator.comparing(Student::getName));
                break;
            case "birthDate":
                studentStream = studentStream.sorted(Comparator.comparing(Student::getBirthdate));
                break;
            default:
                break;
        }

        List<StudentDTO> studentDTOs = studentStream.map(mapper::studentToStudentDTO).collect(Collectors.toList());
        return new ResponseEntity<>(studentDTOs, HttpStatus.OK);
    }


    @GetMapping("/student/total/{id}")
    public ResponseEntity<TotalDTO> totalCoursesEnrolled(@PathVariable(name = "id") Long id){
        TotalDTO totalDTO = new TotalDTO(studentService.totalCoursesEnrolled(id));
        return  new ResponseEntity<>(totalDTO,HttpStatus.OK);
    }
    @DeleteMapping("/student/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id){
        return new ResponseEntity<>(studentService.delete(id),HttpStatus.OK);
    }

    @GetMapping("/student/filter/{basis}")
    public ResponseEntity<List<AddStudentDTO>> filterByBirthDate(@RequestBody DateTimeDTO dateTimeDTO, @PathVariable(name = "basis") String basis){
        return new ResponseEntity<>(
                studentService.filterByBirthDate(dateTimeDTO.getBirthDate(), basis).stream().map(iMapper::studentToAddStudentDTO)
                        .collect(Collectors.toList()),HttpStatus.OK
        );
    }

    @GetMapping("/student/page/{pageNumber}/{pageSize}/{field}")
    public ResponseEntity<List<AddStudentDTO>> getWithPaginationAndSorting(@PathVariable(name = "pageNumber") int pageNumber,
                                                                           @PathVariable(name = "pageSize") int pageSize,
                                                                           @PathVariable(name = "field") String field){
        return new ResponseEntity<>(studentService.getWithPaginationAndSorting(pageNumber, pageSize, field).get()
                                .map(mapper::studentToAddStudentDTO)
                                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @PutMapping("/student/unenroll/{id}")
    public ResponseEntity<StudentDTO> unenrollFromCourse(@PathVariable(name = "id") Long id,
                                                            @RequestBody AddCourseInStudentDTO addCourseInStudentDTO){
        return new ResponseEntity<>(mapper.studentToStudentDTO(studentService.unenrollCourse(id, addCourseInStudentDTO)),HttpStatus.OK);
    }


}
