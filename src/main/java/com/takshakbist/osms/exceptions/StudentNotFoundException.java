package com.takshakbist.osms.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentNotFoundException extends RuntimeException{
    private String message;
    public StudentNotFoundException(String message) {
        this.message = message;
    }
}
