package com.github.StudentsDreamTeam.dto;

import com.github.StudentsDreamTeam.model.Task;
import com.github.StudentsDreamTeam.model.TaskPointer;
import com.github.StudentsDreamTeam.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public record TaskDTO (Integer id,
                       String title,
                       String description,
                       String status,
                       String priority,
                       Long difficulty,
                       UserDTO author,
                       UserDTO executor,
                       LocalDateTime updateDate,
                       Integer fastDoneBonus,
                       Boolean combo,
                       Integer rewardXp,
                       Integer rewardCurrency,
                       LocalDateTime deadline){
    public static TaskDTO fromORM(Task task) {
        return new TaskDTO(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(),
                task.getPriority(), task.getDifficulty(), UserDTO.fromORM(task.getAuthor()), UserDTO.fromORM(task.getExecutor()), 
                task.getUpdateDate(), task.getFastDoneBonus(), task.getCombo(), task.getRewardXp(), task.getRewardCurrency(), task.getDeadline());
    }
}
