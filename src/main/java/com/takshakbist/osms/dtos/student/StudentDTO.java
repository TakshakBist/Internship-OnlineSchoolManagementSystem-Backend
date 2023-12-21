package com.takshakbist.osms.dtos.student;

import com.takshakbist.osms.dtos.course.AddCourseDTO;
import com.takshakbist.osms.utility.Updatable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDTO implements Updatable {
    private Long studentId;
    private String name;
    private String address;
    private LocalDate birthdate;
    private Set<AddCourseDTO> courses = new HashSet<>();
}
