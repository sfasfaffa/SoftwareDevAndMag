package com.example.system.entity;

import lombok.*;


import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "training_plan")
@Builder
public class TrainingPlan {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Lecturer lecturer;
    @Column
    private String courseName;
    @JoinColumn(name = "tra_con_id")
    @OneToOne
    private TrainingContent trainingContent;
    @JoinColumn(name = "tra_fee_id")
    @OneToOne
    private TrainingFee trainingFee;
    @JoinColumn(name = "tra_app_id")
    @OneToOne
    private TrainingApply trainingApply;
    @JoinColumn(name = "tra_com_sum_id")
    @OneToOne
    private TrainingCommentSummary trainingCommentSummary;

    @OneToMany
    private List<RegisterTable> registerTableList;

    @OneToMany(mappedBy = "trainingPlan",cascade = {CascadeType.REMOVE})
    private List<SignInTable> signInTableList;
    @Column
    private Integer trainingNum;
    @Column
    private String startTime;
    @Column
    private String endTime;
    @Column
    private String trainingPlace;
    @JoinColumn(name = "executor_id")
    @ManyToOne
    private Executor executor;
    @Column
    private boolean submit;
}
