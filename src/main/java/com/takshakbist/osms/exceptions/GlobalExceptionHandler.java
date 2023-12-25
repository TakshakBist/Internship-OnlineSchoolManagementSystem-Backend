package com.takshakbist.osms.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ClassroomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleClassroomNotFoundException(ClassroomNotFoundException ex) {
        logger.error(ex.getMessage());
        return new ResponseEntity<>("Classroom not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OverlappingCoursesException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleOverlappingCoursesException(OverlappingCoursesException ex) {
        logger.error(ex.getMessage());
        return new ResponseEntity<>("Courses are overlapping", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CourseNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleCourseNotFoundException(CourseNotFoundException ex) {
        logger.error(ex.getMessage());
        return new ResponseEntity<>("Course not found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleStudentNotFoundException(StudentNotFoundException ex) {
        logger.error(ex.getMessage());
        return new ResponseEntity<>("Student Not Found", HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(TeacherNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleTeacherNotFoundException(TeacherNotFoundException ex) {
        logger.error(ex.getMessage());
        return new ResponseEntity<>("Teacher not Found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CourseCapacityIsFullException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleCourseCapacityIsFullException(CourseCapacityIsFullException ex) {
        logger.error(ex.getMessage());
        return new ResponseEntity<>("Course Capacity is full", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ClassroomNotFreeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleClassroomNotFreeException(ClassroomNotFreeException ex) {
        logger.error(ex.getMessage());
        return new ResponseEntity<>("Classroom Not Free at that time slot", HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        logger.error(ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<>("An internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
