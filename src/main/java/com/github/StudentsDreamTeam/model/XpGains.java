package com.github.StudentsDreamTeam.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "xp_gains")
@Getter
@Setter
public class XpGains {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "date")
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public XpGains(){
    }

    public XpGains(User user, Integer amount, LocalDateTime date) {
        this.user = user;
        this.amount = amount;
        this.date = date;
    }
}
