package com.spd.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "История_версий")
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String status;

    private String priority;

    private Integer difficulty;

    @ManyToOne
    @JoinColumn(name = "автор")
    private User author; 

    @ManyToOne
    @JoinColumn(name = "исполнитель")
    private User executor;  

    @Column(name = "дата_обновления")
    private Date updateDate;

    @Column(name = "бонус_за_быстрое_выполнение")
    private Integer bonusForFastCompletion;

    private Boolean combo;

    @Column(name = "награда_xp")
    private Integer rewardXp;

    @Column(name = "награда_валюта")
    private Integer rewardCurrency;

    private Date deadline;
}
