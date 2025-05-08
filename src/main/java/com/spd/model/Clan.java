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
@Table(name = "Кланы")
@Getter
@Setter
public class Clan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "лидер")
    private User leader;

    @Column(name = "дата_создания")
    private Date creationDate;

    private Integer rating;

    @ManyToOne
    @JoinColumn(name = "проект")
    private Project project;
}
