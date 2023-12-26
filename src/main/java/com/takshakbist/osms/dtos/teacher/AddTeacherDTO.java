package com.takshakbist.osms.dtos.teacher;

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
public class AddTeacherDTO implements Updatable {
    private Long teacherId;
    private String name;
    private String address;
    private LocalDate joinDate;
}
