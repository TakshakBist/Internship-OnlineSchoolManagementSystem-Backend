package com.takshakbist.osms.dtos.teacher;

import com.takshakbist.osms.dtos.course.AddCourseDTO;
import com.takshakbist.osms.utility.Updatable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCourseInTeacherDTO implements Updatable {
    private Set<AddCourseDTO> courses = new HashSet<>();
}
