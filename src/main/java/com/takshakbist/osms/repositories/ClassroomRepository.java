package com.takshakbist.osms.repositories;


import com.takshakbist.osms.entities.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassroomRepository extends JpaRepository<Classroom,Long> {
}
