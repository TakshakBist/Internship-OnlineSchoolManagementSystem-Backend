package com.takshakbist.osms.repositories;


import com.takshakbist.osms.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student,Long> {
}
