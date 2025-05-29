package com.github.StudentsDreamTeam.model;

import com.github.StudentsDreamTeam.dto.TaskDTO;
import com.github.StudentsDreamTeam.dto.UserDTO;
import com.github.StudentsDreamTeam.enums.Difficulty;
import com.github.StudentsDreamTeam.enums.Priority;
import com.github.StudentsDreamTeam.enums.Status;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testTaskCreation() {
        Task task = new Task();
        assertNotNull(task);
        assertNull(task.getId());
        assertNull(task.getTitle());
        assertNull(task.getDescription());
        assertNull(task.getStatus());
        assertNull(task.getPriority());
        assertNull(task.getDifficulty());
        assertNull(task.getAuthor());
        assertNull(task.getExecutor());
        assertNull(task.getUpdateDate());
        assertNull(task.getFastDoneBonus());
        assertNull(task.getCombo());
        assertNull(task.getRewardXp());
        assertNull(task.getRewardCurrency());
        assertNull(task.getDeadline());
        assertNull(task.getSphere());
        assertNull(task.getDuration());
        assertNull(task.getTaskPointer());
        assertNull(task.getAppliedXpReward());
        assertNull(task.getAppliedCurrencyReward());
    }

    @Test
    void testTaskFromDTO() {
        LocalDateTime now = LocalDateTime.now();
        UserDTO authorDTO = new UserDTO(1, "Author", "author@test.com", "password", 100, 1, now, now, 5);
        UserDTO executorDTO = new UserDTO(2, "Executor", "executor@test.com", "password", 200, 2, now, now, 3);

        TaskDTO taskDTO = new TaskDTO(
            1,
            "Test Task",
            "Test Description",
            Status.NEW.getValue(),
            Priority.HIGH.getValue(),
            Difficulty.NORMAL.getLevel(),
            authorDTO,
            executorDTO,
            now,
            10,
            true,
            100,
            50,
            now.plusDays(1),
            "Test Sphere",
            3600L,
            null
        );

        Task task = Task.fromDTO(taskDTO);

        assertEquals(taskDTO.title(), task.getTitle());
        assertEquals(taskDTO.description(), task.getDescription());
        assertEquals(Status.NEW, task.getStatus());
        assertEquals(Priority.HIGH, task.getPriority());
        assertEquals(Difficulty.NORMAL, task.getDifficulty());
        assertNotNull(task.getAuthor());
        assertNotNull(task.getExecutor());
        assertEquals(taskDTO.updateDate(), task.getUpdateDate());
        assertEquals(taskDTO.fastDoneBonus(), task.getFastDoneBonus());
        assertEquals(taskDTO.combo(), task.getCombo());
        assertEquals(taskDTO.rewardXp(), task.getRewardXp());
        assertEquals(taskDTO.rewardCurrency(), task.getRewardCurrency());
        assertEquals(taskDTO.deadline(), task.getDeadline());
        assertEquals(taskDTO.sphere(), task.getSphere());
        assertEquals(Duration.ofSeconds(taskDTO.duration()), task.getDuration());
    }

    @Test
    void testTaskFromDTOWithNullValues() {
        LocalDateTime now = LocalDateTime.now();
        UserDTO authorDTO = new UserDTO(1, "Author", "author@test.com", "password", 100, 1, now, now, 5);

        TaskDTO taskDTO = new TaskDTO(
            null,
            "Test Task",
            null,
            null,
            null,
            null,
            authorDTO,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );

        Task task = Task.fromDTO(taskDTO);

        // Проверяем дефолтные значения
        assertEquals(Status.NEW, task.getStatus());
        assertEquals(Priority.LOW, task.getPriority());
        assertEquals(Difficulty.EASY, task.getDifficulty());
        assertEquals(0, task.getFastDoneBonus());
        assertFalse(task.getCombo());
        assertEquals(0, task.getRewardXp());
        assertEquals(0, task.getRewardCurrency());

        // Проверяем null значения
        assertNull(task.getId());
        assertNull(task.getDescription());
        assertNull(task.getExecutor());
        assertNull(task.getUpdateDate());
        assertNull(task.getDeadline());
        assertNull(task.getSphere());
        assertNull(task.getDuration());
        assertNull(task.getTaskPointer());
    }

    @Test
    void testTaskWithLinkedTask() {
        Task task = new Task();
        TaskPointer pointer = new TaskPointer(1);
        task.setTaskPointer(pointer);

        assertNotNull(task.getTaskPointer());
        assertNull(task.getTaskPointer().getId());
    }

    @Test
    void testTaskEnums() {
        Task task = new Task();

        // Test Status
        task.setStatus(Status.NEW);
        assertEquals(Status.NEW, task.getStatus());
        task.setStatus(Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, task.getStatus());

        // Test Priority
        task.setPriority(Priority.LOW);
        assertEquals(Priority.LOW, task.getPriority());
        task.setPriority(Priority.HIGH);
        assertEquals(Priority.HIGH, task.getPriority());

        // Test Difficulty
        task.setDifficulty(Difficulty.EASY);
        assertEquals(Difficulty.EASY, task.getDifficulty());
        task.setDifficulty(Difficulty.HARD);
        assertEquals(Difficulty.HARD, task.getDifficulty());
    }

    @Test
    void testTaskRewards() {
        Task task = new Task();
        
        // Test XP rewards
        task.setRewardXp(100);
        assertEquals(100, task.getRewardXp());
        task.setAppliedXpReward(90L);
        assertEquals(90L, task.getAppliedXpReward());

        // Test Currency rewards
        task.setRewardCurrency(50);
        assertEquals(50, task.getRewardCurrency());
        task.setAppliedCurrencyReward(45L);
        assertEquals(45L, task.getAppliedCurrencyReward());

        // Test Fast Done Bonus
        task.setFastDoneBonus(10);
        assertEquals(10, task.getFastDoneBonus());

        // Test Combo
        task.setCombo(true);
        assertTrue(task.getCombo());
    }

    @Test
    void testTaskDates() {
        Task task = new Task();
        LocalDateTime now = LocalDateTime.now();
        
        // Test Update Date
        task.setUpdateDate(now);
        assertEquals(now, task.getUpdateDate());

        // Test Deadline
        LocalDateTime deadline = now.plusDays(1);
        task.setDeadline(deadline);
        assertEquals(deadline, task.getDeadline());

        // Test Duration
        Duration duration = Duration.ofHours(2);
        task.setDuration(duration);
        assertEquals(duration, task.getDuration());
    }
} 