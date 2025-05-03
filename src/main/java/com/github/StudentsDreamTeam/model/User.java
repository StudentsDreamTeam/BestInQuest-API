package com.github.StudentsDreamTeam.model;

import com.github.StudentsDreamTeam.dto.UserDTO;
import jakarta.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "xp")
    private Integer xp;

    @Column(name = "level")
    private Integer level;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "last_online_date")
    private LocalDateTime lastInDate;

    @Column(name = "streak")
    private Integer streak;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Project> projects;

    public static User fromDTO(UserDTO userDTO) {
        if (userDTO == null) return null;

        User user = new User();
        user.setName(userDTO.name());
        user.setXp(userDTO.xp());
        user.setLevel(userDTO.level());
        user.setLastInDate(userDTO.lastInDate());
        user.setRegistrationDate(userDTO.registrationDate());
        user.setEmail(userDTO.email());
        user.setPassword(userDTO.password());
        user.setStreak(userDTO.streak());

        return user;
    }
}
