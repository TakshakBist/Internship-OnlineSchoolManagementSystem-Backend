package com.takshakbist.osms.controllers;

import com.takshakbist.osms.dtos.classroom.AddClassroomDTO;
import com.takshakbist.osms.dtos.course.AddCourseDTO;
import com.takshakbist.osms.dtos.course.AddCourseInClassroomDTO;
import com.takshakbist.osms.dtos.student.AddCourseInStudentDTO;
import com.takshakbist.osms.dtos.student.AddStudentDTO;
import com.takshakbist.osms.dtos.student.StudentDTO;
import com.takshakbist.osms.dtos.teacher.AddCourseInTeacherDTO;
import com.takshakbist.osms.dtos.teacher.AddTeacherDTO;
import com.takshakbist.osms.dtos.teacher.TeacherDTO;
import com.takshakbist.osms.entities.Classroom;
import com.takshakbist.osms.entities.Course;
import com.takshakbist.osms.entities.Student;
import com.takshakbist.osms.entities.Teacher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IMapper {
     AddStudentDTO studentToAddStudentDTO(Student student);
     Student addStudentDTOToStudent(AddStudentDTO addStudentDTO);
     AddCourseInStudentDTO studentToAddCourseInStudentDTO(Student student);
     Student addCourseInStudentDTOToStudent(AddCourseInStudentDTO addCourseInStudentDTO);
     Student studentDTOToStudent(StudentDTO studentDTO);
     StudentDTO studentToStudentDTO(Student student);
     AddTeacherDTO teacherToAddTeacherDTO(Teacher teacher);
     Teacher addTeacherDTOToTeacher(AddTeacherDTO addTeacherDTO);
     AddCourseInTeacherDTO teacherToAddCourseInTeacherDTO(Teacher teacher);
     Teacher addCourseInTeacherDTOToTeacher(AddCourseInTeacherDTO addCourseInTeacherDTO);
     Teacher teacherDTOToTeacher(TeacherDTO teacherDTO);
     TeacherDTO teacherToTeacherDTO(Teacher teacher);
     AddClassroomDTO classroomToAddClassroomDTO(Classroom classroom);
     Classroom addClassroomDTOToCLassroom(AddClassroomDTO addClassroomDTO);
     AddCourseDTO courseToAddCourseDTO(Course course);
     Course addCourseDTOToCourse(AddCourseDTO courseDTO);
     AddCourseInClassroomDTO courseToAddCourseInClassroomDTO(Course course);
     Course addCourseInClassroomDTOToCourse(AddCourseInClassroomDTO addCourseInClassroomDTO);

}
