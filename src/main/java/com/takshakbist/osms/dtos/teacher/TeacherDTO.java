package com.takshakbist.osms.dtos.teacher;

import com.takshakbist.osms.dtos.course.AddCourseDTO;
import com.takshakbist.osms.utility.Updatable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO implements Updatable {
    private Long teacherId;
    private String name;
    private String address;
    private LocalDate joinDate;
    private Set<AddCourseDTO> courses = new HashSet<>();
}
