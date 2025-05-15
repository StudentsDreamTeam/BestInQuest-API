package com.github.StudentsDreamTeam.model;

import com.github.StudentsDreamTeam.dto.TaskDTO;
import com.github.StudentsDreamTeam.dto.UserDTO;
import com.github.StudentsDreamTeam.enums.Difficulty;
import com.github.StudentsDreamTeam.enums.Priority;
import com.github.StudentsDreamTeam.enums.Status;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import java.util.ArrayList;
import java.util.List;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private Priority priority;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "difficulty")
    private Difficulty difficulty;

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

    @Column(name = "sphere")
    private String sphere;

    @Column(name = "duration")
    private Duration duration;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "linked_task_id")
    private TaskPointer taskPointer;

    @OneToMany(mappedBy = "linkedTask", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskPointer> taskPointers = new ArrayList<>();

    @Column(name = "applied_xp_reward")
    private Long appliedXpReward;

    @Column(name = "applied_currency_reward")
    private Long appliedCurrencyReward;

    public static Task fromDTO(TaskDTO taskDTO) {
        Task result = new Task();
        result.setTitle(taskDTO.title());
        result.setDescription(taskDTO.description());
        result.setStatus(
                taskDTO.status() != null
                        ? Status.fromValue(taskDTO.status())
                        : Status.NEW
        );

        result.setPriority(
                taskDTO.priority() != null
                        ? Priority.fromValue(taskDTO.priority())
                        : Priority.LOW
        );

        result.setDifficulty(
                taskDTO.difficulty() != null
                        ? Difficulty.fromValue(taskDTO.difficulty())
                        : Difficulty.EASY
        );

        result.setAuthor(User.fromDTO(taskDTO.author()));
        result.setExecutor(User.fromDTO(taskDTO.executor()));
        result.setUpdateDate(taskDTO.updateDate());
        result.setFastDoneBonus(taskDTO.fastDoneBonus() != null ? taskDTO.fastDoneBonus() : 0);
        result.setCombo(taskDTO.combo() != null ? taskDTO.combo() : false);
        result.setRewardXp(taskDTO.rewardXp() != null ? taskDTO.rewardXp() : 0);
        result.setRewardCurrency(taskDTO.rewardCurrency() != null ? taskDTO.rewardCurrency() : 0);
        result.setDeadline(taskDTO.deadline());
        result.setSphere(taskDTO.sphere());
        result.setDuration(
                taskDTO.duration() != null
                        ? Duration.ofSeconds(taskDTO.duration())
                        : null
        );
        if (taskDTO.linkedTaskId() != null) {
            result.setTaskPointer(new TaskPointer(taskDTO.linkedTaskId()));
        }

        return result;
    }
}
