package com.takshakbist.osms.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassroomNotFoundException extends RuntimeException{
    private String message;
    public ClassroomNotFoundException(String message) {
        this.message = message;
    }

}
