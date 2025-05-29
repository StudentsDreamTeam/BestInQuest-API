package com.github.StudentsDreamTeam.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "level_requirements")
@Data
@Getter
@Setter

public class LevelRequirement {

    @Id
    private Integer level;

    @Column(name = "required_xp", nullable = false)
    private Integer requiredXp;
}
