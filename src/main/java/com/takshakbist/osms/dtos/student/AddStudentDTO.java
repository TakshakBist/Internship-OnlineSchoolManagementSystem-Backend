package com.takshakbist.osms.dtos.student;

import com.takshakbist.osms.utility.Updatable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddStudentDTO implements Updatable {
    private Long studentId;
    private String name;
    private String address;
    private LocalDate birthdate;
}
