package com.takshakbist.osms.dtos.student;

import com.takshakbist.osms.dtos.course.AddCourseDTO;
import com.takshakbist.osms.utility.Updatable;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddCourseInStudentDTO implements Updatable {
    private Set<AddCourseDTO> courses = new HashSet<>();
}
