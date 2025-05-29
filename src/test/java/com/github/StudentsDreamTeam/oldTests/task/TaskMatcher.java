package com.github.StudentsDreamTeam.task;

import com.github.StudentsDreamTeam.model.Task;
import org.mockito.ArgumentMatcher;

public record TaskMatcher(Task original) implements ArgumentMatcher<Task> {
    @Override
    public boolean matches(Task task) {
        return task != null &&
                task.getTitle().equals(original.getTitle()) &&
                task.getDescription().equals(original.getDescription()) &&
                task.getDeadline().equals(original.getDeadline()) &&
                task.getDuration().equals(original.getDuration()) &&
                task.getSphere().equals(original.getSphere()) &&
                task.getCombo().equals(original.getCombo()) &&
                task.getPriority().equals(original.getPriority()) &&
                task.getDifficulty().equals(original.getDifficulty()) &&
                task.getStatus().equals(original.getStatus()) &&
                task.getRewardCurrency().equals(original.getRewardCurrency()) &&
                task.getRewardXp().equals(original.getRewardXp()) &&
                task.getFastDoneBonus().equals(original.getFastDoneBonus()) &&
                task.getUpdateDate().equals(original.getUpdateDate()) &&
                task.getAuthor().getId().equals(original.getAuthor().getId()) &&
                task.getExecutor().getId().equals(original.getExecutor().getId());
    }
}
