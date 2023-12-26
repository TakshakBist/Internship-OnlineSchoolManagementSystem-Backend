package com.takshakbist.osms.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClassroomNotFreeException extends RuntimeException{
    private String message;
    public ClassroomNotFreeException(String message) {
        this.message = message;
    }
}
