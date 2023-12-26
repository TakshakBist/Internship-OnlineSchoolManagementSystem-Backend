package com.takshakbist.osms.entities;

import com.takshakbist.osms.utility.Updatable;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Student implements Updatable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;
    private String name;
    private String address;
    private LocalDate birthdate;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST}, targetEntity = Course.class)
    @JoinTable(name = "student_course",
            inverseJoinColumns = @JoinColumn(name = "course_id", nullable = false, updatable = false),
            joinColumns = @JoinColumn(name = "student_id", nullable = false, updatable = false),
            foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT),
            inverseForeignKey = @ForeignKey(ConstraintMode.CONSTRAINT))
    private Set<Course> courses = new HashSet<>();

}