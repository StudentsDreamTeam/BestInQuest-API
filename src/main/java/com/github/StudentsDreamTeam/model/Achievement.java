package com.github.StudentsDreamTeam.model;

import com.github.StudentsDreamTeam.dto.AchievementDTO;
import com.github.StudentsDreamTeam.dto.TaskDTO;
import com.github.StudentsDreamTeam.dto.UserDTO;
import com.github.StudentsDreamTeam.enums.AchievementType;
import com.github.StudentsDreamTeam.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Type;
import java.util.List;

@Entity
@Table(name = "Achievements")
@Getter
@Setter
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "required_value")
    private Integer required_value;

    @Column(name = "icon")
    private String icon;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private AchievementType type;

    @OneToMany(mappedBy = "achievement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserAchievement> userAchievements;

    public static Achievement fromDTO(AchievementDTO achievementDTO) {
        Achievement achievement = new Achievement();

        achievement.setName(achievementDTO.name());
        achievement.setDescription(achievementDTO.description());
        achievement.setType(
                achievementDTO.type() != null
                        ? AchievementType.fromValue(achievementDTO.type())
                        : AchievementType.XP
        );
        achievement.setRequired_value(achievementDTO.requiredXp());
        achievement.setIcon(achievementDTO.icon());
        return achievement;
    }
}
