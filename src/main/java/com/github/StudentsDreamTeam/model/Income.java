package com.github.StudentsDreamTeam.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "income")
@Getter
@Setter
public class Income {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "description")
    private String description;

    public Income(){
    }

    public Income(User user, Integer amount, LocalDateTime date, String description) {
        this.user = user;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }
}

