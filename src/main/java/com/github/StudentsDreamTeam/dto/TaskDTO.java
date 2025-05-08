package com.github.StudentsDreamTeam.dto;

import com.github.StudentsDreamTeam.model.Task;

import java.time.LocalDateTime;

public record TaskDTO (Integer id,
                       String title,
                       String description,
                       String status,
                       String priority,
                       Integer difficulty,
                       UserDTO author,
                       UserDTO executor,
                       LocalDateTime updateDate,
                       Integer fastDoneBonus,
                       Boolean combo,
                       Integer rewardXp,
                       Integer rewardCurrency,
                       LocalDateTime deadline,
                       String sphere,
                       Long duration,
                       Integer linkedTaskId) {

    public static TaskDTO fromORM(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus() != null ? task.getStatus().getValue() : null,
                task.getPriority() != null ? task.getPriority().getValue() : null,
                task.getDifficulty() != null ? task.getDifficulty().getLevel() : null,
                UserDTO.fromORM(task.getAuthor()),
                UserDTO.fromORM(task.getExecutor()),
                task.getUpdateDate(),
                task.getFastDoneBonus(),
                task.getCombo(),
                task.getRewardXp(),
                task.getRewardCurrency(),
                task.getDeadline(),
                task.getSphere(),
                task.getDuration(),
                task.getTaskPointer() != null ? task.getTaskPointer().getId() : null
        );
    }
}
