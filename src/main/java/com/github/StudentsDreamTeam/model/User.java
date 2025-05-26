package com.github.StudentsDreamTeam.model;

import com.github.StudentsDreamTeam.dto.UserDTO;
import com.github.StudentsDreamTeam.enums.Status;
import jakarta.persistence.*;

import lombok.Data;

import java.time.LocalDateTime;
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

    @Column(name = "email", unique = true, nullable = false)
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UsersInventory> inventory;

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

    public long getCompletedTasksCount() {
        if (tasks == null) return 0;
        return tasks.stream().filter(task -> task.getStatus() == Status.DONE).count();
    }

    public long getInventoryItems() {
        if (inventory == null) return 0;
        return inventory.stream()
                .mapToLong(UsersInventory::getAmount)
                .sum();
    }

}
