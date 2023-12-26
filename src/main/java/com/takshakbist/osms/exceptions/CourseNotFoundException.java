package com.takshakbist.osms.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseNotFoundException extends RuntimeException{
    private String message;
    public CourseNotFoundException(String message) {
        this.message = message;
    }
}
