package com.github.StudentsDreamTeam.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Version_history")
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "priority")
    private String priority;

    @Column(name = "difficulty")
    private Long difficulty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    private User author; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executor")
    private User executor;  

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "fast_done_bonus")
    private Integer fastDoneBonus;

    @Column(name = "combo")
    private Boolean combo;

    @Column(name = "reward_xp")
    private Integer rewardXp;

    @Column(name = "reward_currency")
    private Integer rewardCurrency;

    @Column(name = "deadline")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linked_task_id")
    private TaskPointer taskPointer;

}
