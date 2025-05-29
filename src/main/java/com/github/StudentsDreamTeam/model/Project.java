package com.github.StudentsDreamTeam.model;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Projects")
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    private User owner;

    @Column(name = "status")
    private String status;

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime creationDate;

    @Column(name = "project_items")
    private Long projectItems;

    @Column(name = "done")
    private Boolean done;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<TaskPointer> taskPointers = new ArrayList<>();

}
