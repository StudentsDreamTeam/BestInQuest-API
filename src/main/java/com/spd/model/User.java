package com.spd.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import lombok.Data;

import java.util.Date;


@Entity
@Table(name = "Пользователи")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private Integer xp;

    private Integer level;

    @Column(name = "дата_регистрации")
    private Date registrationDate;

    @Column(name = "дата_последнего_входа")
    private Date lastInDate;

    private Integer streak;

}
