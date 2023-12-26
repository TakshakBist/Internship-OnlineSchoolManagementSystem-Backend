package com.takshakbist.osms.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherNotFoundException extends RuntimeException{
    private String message;
    public TeacherNotFoundException(String message) {
        this.message = message;
    }
}
