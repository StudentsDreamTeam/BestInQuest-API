package com.spd.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Комментарии")
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "задача")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "пользователь")
    private User user;

    private String text;

    @Column(name = "дата_создания")
    private Date creationDate;
}
