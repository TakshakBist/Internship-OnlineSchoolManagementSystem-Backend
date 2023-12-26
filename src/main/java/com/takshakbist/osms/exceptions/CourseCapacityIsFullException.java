package com.takshakbist.osms.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseCapacityIsFullException extends RuntimeException{
    private String message;
    public CourseCapacityIsFullException(String message) {
        this.message = message;
    }
}
