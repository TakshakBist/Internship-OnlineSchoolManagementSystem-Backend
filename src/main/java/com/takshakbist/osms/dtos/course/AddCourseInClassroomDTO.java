package com.takshakbist.osms.dtos.course;

import com.takshakbist.osms.dtos.classroom.AddClassroomDTO;
import com.takshakbist.osms.utility.Updatable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddCourseInClassroomDTO implements Updatable {
    private AddClassroomDTO classroom ;
}
