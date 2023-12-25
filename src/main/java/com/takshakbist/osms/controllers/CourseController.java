package com.takshakbist.osms.controllers;

import com.takshakbist.osms.dtos.DateTimeDTO;
import com.takshakbist.osms.dtos.course.AddCourseDTO;
import com.takshakbist.osms.dtos.course.AddCourseInClassroomDTO;
import com.takshakbist.osms.entities.Course;
import com.takshakbist.osms.services.Impl.CourseServiceImpl;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CourseController {
    @NonNull
    private final CourseServiceImpl courseService;

    @NonNull
    private final IMapper mapper;


    @PostMapping("/course")
    public ResponseEntity<AddCourseDTO> add(@RequestBody AddCourseDTO addCourseDTO){
        Course course = courseService.add(addCourseDTO);
        return new ResponseEntity<>(mapper.courseToAddCourseDTO(course), HttpStatus.CREATED);
    }

    @PutMapping("/course/{id}")
    public ResponseEntity<AddCourseDTO> update(@PathVariable(name = "id") Long id,
                                               @RequestBody AddCourseDTO addCourseDTO){
        Course course = courseService.update(id, addCourseDTO);
        return new ResponseEntity<>(mapper.courseToAddCourseDTO(course),HttpStatus.OK);
    }

    @PutMapping("/course/add/{id}")
    public ResponseEntity<AddCourseInClassroomDTO> addInClassroom(@PathVariable(name = "id") Long id,
                                                                  @RequestBody AddCourseInClassroomDTO addCourseInClassroomDTO){
        Course course = courseService.addInClassroom(id,addCourseInClassroomDTO);
        return new ResponseEntity<>(mapper.courseToAddCourseInClassroomDTO(course),HttpStatus.OK);
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<AddCourseDTO> getById(@PathVariable(name = "id") Long id){
        return new ResponseEntity<>(mapper.courseToAddCourseDTO(courseService.getById(id)),HttpStatus.OK);
    }

    @GetMapping("/course/get/{field}")
    public ResponseEntity<List<AddCourseDTO>> getAll(@PathVariable(name = "field") String field){

        return new ResponseEntity<>(courseService.getAll(field)
                .stream()
                .map(mapper::courseToAddCourseDTO)
                .collect(Collectors.toList()),HttpStatus.OK);
    }

    @GetMapping("/course/filter/{field}/{basis}")
    public ResponseEntity<List<AddCourseDTO>> filter(@RequestBody DateTimeDTO dateTimeDTO,
                                                     @PathVariable(name = "field") String field,
                                                     @PathVariable(name = "basis") String basis){
        List<AddCourseDTO> addCourseDTOS;
        if (field.equals("startDate")){
            addCourseDTOS = courseService.filterByStartDate(dateTimeDTO.getStartDate(), basis).stream().map(mapper::courseToAddCourseDTO).collect(Collectors.toList());
        }
        else if (field.equals("startTime")){
            addCourseDTOS = courseService.filterByStartTime(dateTimeDTO.getStartTime(), basis).stream().map(mapper::courseToAddCourseDTO).collect(Collectors.toList());
        }
        else {
            addCourseDTOS = courseService.filterByEndTime(dateTimeDTO.getEndTime(), basis).stream().map(mapper::courseToAddCourseDTO).collect(Collectors.toList());
        }
        return new ResponseEntity<>(addCourseDTOS,HttpStatus.OK);
    }

    @DeleteMapping("course/{id}")
    public ResponseEntity<String> delete(@PathVariable(name = "id") Long id){
        return new ResponseEntity<>(courseService.delete(id),HttpStatus.OK);
    }

    @GetMapping("course/page/{pageNumber}/{pageSize}/{field}")
    public ResponseEntity<List<AddCourseDTO>> getWithPaginationAndSorting(@PathVariable(name = "pageNumber") int pageNumber,
                                                                          @PathVariable(name = "pageSize") int pageSize,
                                                                          @PathVariable(name = "field") String field){
        return new ResponseEntity<>(courseService.getWithPaginationAndSorting(pageNumber, pageSize, field).get()
                .map(mapper::courseToAddCourseDTO)
                .collect(Collectors.toList()), HttpStatus.OK);
    }
}
