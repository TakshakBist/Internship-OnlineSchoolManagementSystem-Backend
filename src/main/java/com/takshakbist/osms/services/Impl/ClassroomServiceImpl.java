package com.takshakbist.osms.services.Impl;

import com.takshakbist.osms.controllers.IMapper;
import com.takshakbist.osms.dtos.classroom.AddClassroomDTO;
import com.takshakbist.osms.entities.Classroom;
import com.takshakbist.osms.entities.Course;
import com.takshakbist.osms.exceptions.ClassroomNotFoundException;
import com.takshakbist.osms.repositories.ClassroomRepository;
import com.takshakbist.osms.repositories.CourseRepository;
import com.takshakbist.osms.services.ClassroomService;
import com.takshakbist.osms.utility.Utility;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ClassroomServiceImpl implements ClassroomService {

    @NonNull
    private final ClassroomRepository classroomRepository;
    @NonNull
    private final IMapper mapper;
    @NonNull
    private final CourseRepository courseRepository;


    @Override
    public Classroom add(AddClassroomDTO addClassroomDTO) {
        return classroomRepository.save(mapper.addClassroomDTOToCLassroom(addClassroomDTO));
    }

    @Override
    public Classroom update(Long id, AddClassroomDTO addClassroomDTO) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(()-> ClassroomNotFoundException.builder().message("Classroom not found").build());
        if (!Utility.isNull(addClassroomDTO)){
            classroom.setName(addClassroomDTO.getName());
        }
        return classroomRepository.save(classroom);
    }

    @Override
    public Classroom getById(Long id) {
        return classroomRepository.findById(id)
                .orElseThrow(()->ClassroomNotFoundException.builder().message("Classroom not found").build());
    }

    @Override
    public List<Classroom> getAll() {
        return Optional.of(classroomRepository.findAll())
                .orElseThrow(()-> ClassroomNotFoundException.builder().message("Classroom not found").build());
    }

    @Override
    public String delete(Long id) {
        Classroom classroom = classroomRepository.findById(id)
                .orElseThrow(()-> ClassroomNotFoundException.builder().message("Classroom not found").build());
        Set<Course> courseList = classroom.getCourses();
        for (var course: courseList){
            course.setClassroom(null);
            courseRepository.save(course);
        }
        classroomRepository.deleteById(id);
        return "Classroom of id "  + id + " deleted";
    }
}
