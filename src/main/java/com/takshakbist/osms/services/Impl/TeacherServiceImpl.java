package com.takshakbist.osms.services.Impl;

import com.takshakbist.osms.controllers.IMapper;
import com.takshakbist.osms.dtos.course.AddCourseDTO;
import com.takshakbist.osms.dtos.teacher.AddCourseInTeacherDTO;
import com.takshakbist.osms.dtos.teacher.AddTeacherDTO;
import com.takshakbist.osms.entities.Course;
import com.takshakbist.osms.entities.Teacher;
import com.takshakbist.osms.exceptions.CourseNotFoundException;
import com.takshakbist.osms.exceptions.OverlappingCoursesException;
import com.takshakbist.osms.exceptions.TeacherNotFoundException;
import com.takshakbist.osms.repositories.CourseRepository;
import com.takshakbist.osms.repositories.TeacherRepository;
import com.takshakbist.osms.services.TeacherService;
import com.takshakbist.osms.utility.Utility;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TeacherServiceImpl implements TeacherService {
    @NonNull
    private final TeacherRepository teacherRepository;
    @NonNull
    private final IMapper mapper;
    @NonNull
    private final CourseRepository courseRepository;


    @Override
    public Teacher add(AddTeacherDTO addTeacherDTO) {
        return teacherRepository.save(mapper.addTeacherDTOToTeacher(addTeacherDTO));
    }

    @Override
    public Teacher update(Long id, AddTeacherDTO addTeacherDTO) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(()->new TeacherNotFoundException(""));
        if(addTeacherDTO != null){
            teacher.setName(addTeacherDTO.getName());
            teacher.setAddress(addTeacherDTO.getAddress());
            teacher.setJoinDate(addTeacherDTO.getJoinDate());
        }
        return teacherRepository.save(teacher);
    }

    @Override
    @Transactional
    public Teacher teachCourse(Long id, AddCourseInTeacherDTO addCourseInTeacherDTO) {
        Teacher teacher = teacherRepository.findById(id).orElseThrow(()->new TeacherNotFoundException(""));
        Set<AddCourseDTO> courses = addCourseInTeacherDTO.getCourses();

        for (var course : courses){
            Long courseId = course.getCourseId();
            Course course1 = courseRepository.findById(courseId).orElseThrow(()->new CourseNotFoundException(""));

            if (Utility.coursesOverlap(teacher.getCourses(),course1)){
                throw new OverlappingCoursesException("Courses are overlapping, a teacher can't teach two classes at same time");
                }
            teacher.getCourses().add(course1);
        }

        return teacherRepository.save(teacher);
    }

    @Override
    public Teacher getById(Long id) {
        return teacherRepository.findById(id).orElseThrow(()->new TeacherNotFoundException("Teacher of that id not found"));
    }

    @Override
    public List<Teacher> getAll() {
        return Optional.of(teacherRepository.findAll()).orElseThrow(()->new TeacherNotFoundException("No teacher found"));
    }

    @Override
    public String delete(Long id) {
        teacherRepository.findById(id).orElseThrow(()-> new TeacherNotFoundException("Cannot delete, Teacher not found"));
        teacherRepository.deleteById(id);
        return "Teacher of id :" + id + " deleted";
    }

    @Override
    public Long getTotalStudentsEnrolled(Long id) {
        return Utility.totalStudentsEnrolledInTeacherCourse(teacherRepository.findById(id).orElseThrow(()-> new TeacherNotFoundException("Teacher not found")));
    }

    @Override
    public List<Teacher> filterByJoinDate(LocalDate joinDate, String basis) {
        return teacherRepository.findAll().stream()
                .filter(teacher -> basis.equals("before") ? teacher.getJoinDate().isBefore(joinDate) : teacher.getJoinDate().isAfter(joinDate))
                .collect(Collectors.toList());
    }

    @Override
    public Page<Teacher> getWithPaginationAndSorting(Integer pageNumber, Integer pageSize, String field) {
        return teacherRepository.findAll(PageRequest.of(pageNumber,pageSize, Sort.by(field).ascending()));
    }
}
