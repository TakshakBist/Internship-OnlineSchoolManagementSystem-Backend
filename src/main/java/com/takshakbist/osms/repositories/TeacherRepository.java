package com.takshakbist.osms.repositories;


import com.takshakbist.osms.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher,Long> {
}
