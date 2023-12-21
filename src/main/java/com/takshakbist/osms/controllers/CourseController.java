package com.takshakbist.osms.controllers;

import com.takshakbist.osms.dtos.DateTimeDTO;
import com.takshakbist.osms.dtos.course.AddCourseDTO;
import com.takshakbist.osms.dtos.course.AddCourseInClassroomDTO;
import com.takshakbist.osms.entities.Course;
import com.takshakbist.osms.entities.Student;
import com.takshakbist.osms.services.Impl.CourseServiceImpl;
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
    public ResponseEntity<AddCourseDTO> update(@PathVariable(name = "id") Long id, @RequestBody AddCourseDTO addCourseDTO){
        Course course = courseService.update(id, addCourseDTO);
        return new ResponseEntity<>(mapper.courseToAddCourseDTO(course),HttpStatus.OK);
    }

    @PutMapping("/course/add/{id}")
    public ResponseEntity<AddCourseInClassroomDTO> addInClassroom(@PathVariable(name = "id") Long id, @RequestBody AddCourseInClassroomDTO addCourseInClassroomDTO){
        Course course = courseService.addInClassroom(id,addCourseInClassroomDTO);
        return new ResponseEntity<>(mapper.courseToAddCourseInClassroomDTO(course),HttpStatus.OK);
    }

    @GetMapping("/course/{id}")
    public ResponseEntity<AddCourseDTO> getById(@PathVariable(name = "id") Long id){
        return new ResponseEntity<>(mapper.courseToAddCourseDTO(courseService.getById(id)),HttpStatus.OK);
    }

    @GetMapping("/course")
    public ResponseEntity<List<AddCourseDTO>> getAll(@RequestParam(name = "basis", defaultValue = "none") String basis){
        List<Course> courses = courseService.getAll();
        Stream<Course> courseStream = courses.stream();

        switch (basis){
            case "name":
                courseStream=courseStream.sorted(Comparator.comparing(Course::getName));
                break;

            case "startTime":
                courseStream =  courseStream.sorted(Comparator.comparing(Course::getStartTime));
                break;

            case "endTime":
                courseStream = courseStream.sorted(Comparator.comparing(Course::getEndTime));
                break;

            case "startDate":
                courseStream = courseStream.sorted(Comparator.comparing(Course::getStartDate));
                break;

            default:
                break;
        }
        List<AddCourseDTO> courseDTOS = courseStream.map(mapper::courseToAddCourseDTO).collect(Collectors.toList());
        return new ResponseEntity<>(courseDTOS,HttpStatus.OK);
    }

    @GetMapping("/course/filter")
    public ResponseEntity<List<AddCourseDTO>> filter(@RequestBody DateTimeDTO dateTimeDTO, @RequestParam(name = "basis") String basis){
        List<AddCourseDTO> addCourseDTOS;
        if (basis.equals("startDate")){
            addCourseDTOS = courseService.filterByStartDate(dateTimeDTO.getStartDate(), basis).stream().map(mapper::courseToAddCourseDTO).collect(Collectors.toList());
        }
        else if (basis.equals("startTime")){
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

}
