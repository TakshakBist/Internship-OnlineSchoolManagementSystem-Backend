package com.takshakbist.osms.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DateTimeDTO {
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate joinDate;
    private LocalDate birthDate;
}
