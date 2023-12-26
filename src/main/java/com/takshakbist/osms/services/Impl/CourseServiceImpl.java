package com.takshakbist.osms.services.Impl;

import com.takshakbist.osms.controllers.IMapper;
import com.takshakbist.osms.dtos.course.AddCourseDTO;
import com.takshakbist.osms.dtos.course.AddCourseInClassroomDTO;
import com.takshakbist.osms.entities.Classroom;
import com.takshakbist.osms.entities.Course;
import com.takshakbist.osms.exceptions.ClassroomNotFoundException;
import com.takshakbist.osms.exceptions.ClassroomNotFreeException;
import com.takshakbist.osms.exceptions.CourseNotFoundException;
import com.takshakbist.osms.repositories.ClassroomRepository;
import com.takshakbist.osms.repositories.CourseRepository;
import com.takshakbist.osms.services.CourseService;
import com.takshakbist.osms.utility.Utility;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CourseServiceImpl implements CourseService {
     @NonNull
     private final CourseRepository courseRepository;

     @NonNull
     private final IMapper iMapper;

     @NonNull
     private final ClassroomRepository classroomRepository;

    @Override
    public Course add(AddCourseDTO addCourseDTO) {
        return courseRepository.save(iMapper.addCourseDTOToCourse(addCourseDTO));
    }

    @Override
    public Course update(Long id, AddCourseDTO addCourseDTO) {
        Course course = courseRepository.findById(id).orElseThrow(()->CourseNotFoundException.builder().message("Course not found").build());
        if (addCourseDTO != null){
            course.setName(addCourseDTO.getName());
            course.setCapacity(addCourseDTO.getCapacity());
            course.setStartDate(addCourseDTO.getStartDate());
            course.setStartTime(addCourseDTO.getStartTime());
            course.setEndTime(addCourseDTO.getEndTime());
        }
        return courseRepository.save(course);
    }

    @Override
    @Transactional
    public Course addInClassroom(Long id,AddCourseInClassroomDTO addCourseInClassroomDTO) {
        Course course = courseRepository.findById(id).orElseThrow(()->CourseNotFoundException.builder().message("Course not found").build());
        Classroom classroom = classroomRepository.findById(addCourseInClassroomDTO.getClassroom().getClassroomId())
                .orElseThrow(()->ClassroomNotFoundException.builder().message("Classroom not found").build());
        checkIfCoursesOverlapAndThrowException(classroom,course);
        classroom.getCourses().add(course);
        course.setClassroom(classroom);
        return courseRepository.save(course);
    }


    private void checkIfCoursesOverlapAndThrowException(Classroom classroom, Course course){
        if (Utility.coursesOverlap( classroom.getCourses(),course)) {
            throw ClassroomNotFreeException.builder().message("Classroom is not free").build();
        }
    }

    @Override
    public Course getById(Long id) {
        return courseRepository.findById(id).orElseThrow(()->CourseNotFoundException.builder().message("Course is not found").build());
    }

    @Override
    public List<Course> getAll(String field) {
        return Optional.of(courseRepository.findAll(Sort.by(field))).orElseThrow(()-> CourseNotFoundException.builder().message("Course is not found").build());
    }

    @Override
    public String delete(Long id) {
        courseRepository.findById(id).orElseThrow(()->CourseNotFoundException.builder().message("Course of that id not found").build());
        courseRepository.deleteById(id);
        return "Course of id " + id + " deleted";
    }

    @Override
    public List<Course> filterByStartDate(LocalDate startDate, String basis) {
        return courseRepository.findAll().stream()
                .filter(course -> basis.equals("before") ? course.getStartDate().isBefore(startDate) : course.getStartDate().isAfter(startDate))
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> filterByStartTime(LocalTime startTime,String basis){
        return  courseRepository.findAll().stream()
                .filter(course -> basis.equals("before") ?  course.getStartTime().isBefore(startTime) :course.getStartTime().isAfter(startTime))
                .collect(Collectors.toList());
    }

    @Override
    public List<Course> filterByEndTime(LocalTime endTime, String basis) {
        return courseRepository.findAll().stream()
                .filter(course -> {
                    LocalTime courseEndTime = course.getEndTime();
                    return courseEndTime != null && endTime != null &&
                            (basis.equals("before") ? courseEndTime.isBefore(endTime) : courseEndTime.equals(endTime));
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<Course> getWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String field) {
        return courseRepository.findAll(PageRequest.of(pageNumber,pageSize,Sort.by(field).ascending()));
    }


}
