package com.example.system.entity;

import lombok.*;


import javax.persistence.*;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "lecturer")
@Builder
public class Lecturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @OneToMany
    private List<TrainingPlan> TrainingPlanList;
    @Column
    private String name;
    @Column
    private String position;
    @Column
    private String speciality;
    @Column
    private String emailAddress;
    @Column
    private String phone;
}









