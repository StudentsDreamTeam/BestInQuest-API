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
@Table(name = "Проекты")
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "владелец")
    private User owner;

    private String status;

    @Column(name = "дата_создания")
    private Date creationDate;

    @Column(name = "предметы_проекта")
    private Integer projectItems;

    private Boolean completed;

}
