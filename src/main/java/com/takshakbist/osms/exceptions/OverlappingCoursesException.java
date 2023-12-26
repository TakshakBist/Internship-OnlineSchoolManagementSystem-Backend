package com.takshakbist.osms.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OverlappingCoursesException extends RuntimeException{
    private String message;
    public OverlappingCoursesException(String message) {
        this.message = message;
    }
}
