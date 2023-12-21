package com.takshakbist.osms.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.takshakbist.osms.entities.Course;

public interface CourseRepository extends JpaRepository<Course,Long> {
}
