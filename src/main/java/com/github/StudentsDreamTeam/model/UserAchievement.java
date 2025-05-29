package com.github.StudentsDreamTeam.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users_achievements")
@Getter
@Setter
public class UserAchievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "achievement", nullable = false)
    private Achievement achievement;

    @Column(name = "acquire_date", nullable = false)
    private LocalDateTime acquireDate;
}
