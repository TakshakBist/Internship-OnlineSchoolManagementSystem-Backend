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
        Course course = courseRepository.findById(id).orElseThrow(()->new CourseNotFoundException(""));
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
        Course course = courseRepository.findById(id).orElseThrow(()->new CourseNotFoundException(""));

        Classroom classroom = classroomRepository.findById(addCourseInClassroomDTO.getClassroom().getClassroomId())
                .orElseThrow(()->new ClassroomNotFoundException("Classroom of that id was not found"));

        if (Utility.coursesOverlap( classroom.getCourses(),course)) {
            throw new ClassroomNotFreeException("Classroom is not free at that time slot");
        }
        classroom.getCourses().add(course);
        course.setClassroom(classroom);
        return courseRepository.save(course);
    }

    @Override
    public Course getById(Long id) {
        return courseRepository.findById(id).orElseThrow(()->new CourseNotFoundException(""));
    }

    @Override
    public List<Course> getAll() {
        return Optional.of(courseRepository.findAll()).orElseThrow(()-> new CourseNotFoundException(""));
    }

    @Override
    public String delete(Long id) {
        courseRepository.findById(id).orElseThrow(()->new CourseNotFoundException(""));
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
                .filter(course -> basis.equals("before") ? course.getEndTime().isBefore(endTime) : course.getEndTime().equals(endTime))
                .collect(Collectors.toList());
    }

}
