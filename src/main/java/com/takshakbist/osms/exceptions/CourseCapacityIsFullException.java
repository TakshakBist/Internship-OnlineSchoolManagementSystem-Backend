package com.takshakbist.osms.exceptions;

public class CourseCapacityIsFullException extends RuntimeException{
    public CourseCapacityIsFullException(String message) {
        super(message);
    }
}
