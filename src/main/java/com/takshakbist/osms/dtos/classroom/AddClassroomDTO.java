package com.takshakbist.osms.dtos.classroom;

import com.takshakbist.osms.utility.Updatable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddClassroomDTO implements Updatable {
  private Long classroomId;
  private String name;
}
