package com.takshakbist.osms.entities;

import com.takshakbist.osms.utility.Updatable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Course implements Updatable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;
    private String name;
    private Integer capacity;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalTime endTime;


    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @JoinTable(name = "course_teacher",
            joinColumns = @JoinColumn(name = "course_id", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "teacher_id", nullable = false, updatable = false),
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Set<Teacher> teacher;


    @ManyToMany(fetch = FetchType.LAZY,cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST},
            targetEntity = Student.class)
    @JoinTable(name = "student_course",
            joinColumns = @JoinColumn(name = "course_id", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "student_id", nullable = false, updatable = false),
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Set<Student> students = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Classroom classroom;

}