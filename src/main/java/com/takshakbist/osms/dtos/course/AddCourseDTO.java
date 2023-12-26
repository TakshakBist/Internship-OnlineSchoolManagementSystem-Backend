package com.takshakbist.osms.dtos.course;

import com.takshakbist.osms.utility.Updatable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCourseDTO implements Updatable {

    private Long courseId;
    private String name;
    private Integer capacity;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalTime endTime;
}
