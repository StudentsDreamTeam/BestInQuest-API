package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.dto.TaskDTO;
import com.github.StudentsDreamTeam.enums.Difficulty;
import com.github.StudentsDreamTeam.enums.Priority;
import com.github.StudentsDreamTeam.enums.Status;
import com.github.StudentsDreamTeam.model.Task;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.service.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private Task createTestTask() {
        User testAuthor = new User();
        testAuthor.setId(1);
        testAuthor.setName("Test Author");
        testAuthor.setEmail("author@example.com");

        User testExecutor = new User();
        testExecutor.setId(2);
        testExecutor.setName("Test Executor");
        testExecutor.setEmail("executor@example.com");

        Task testTask = new Task();
        testTask.setId(1);
        testTask.setTitle("Test Task");
        testTask.setDescription("Test Description");
        testTask.setStatus(Status.IN_PROGRESS);
        testTask.setPriority(Priority.HIGH);
        testTask.setDifficulty(Difficulty.HARD);
        testTask.setAuthor(testAuthor);
        testTask.setExecutor(testExecutor);
        testTask.setUpdateDate(LocalDateTime.now());
        testTask.setFastDoneBonus(10);
        testTask.setCombo(false);
        testTask.setRewardXp(100);
        testTask.setRewardCurrency(50);
        testTask.setDeadline(LocalDateTime.now().plusDays(7));
        testTask.setSphere("Test Sphere");
        testTask.setDuration(Duration.ofHours(2));

        return testTask;
    }

    @Test
    void createTask_Success() {
        // Arrange
        Task testTask = createTestTask();
        TaskDTO testTaskDTO = TaskDTO.fromORM(testTask);
        Long authorId = 1L;
        Long executorId = 2L;
        when(taskService.createTask(eq(authorId), eq(executorId), any(Task.class))).thenReturn(testTask);

        // Act
        TaskDTO result = taskController.createTask(authorId, executorId, testTaskDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testTask.getTitle(), result.title());
        assertEquals(testTask.getDescription(), result.description());
        assertEquals(testTask.getStatus().getValue(), result.status());
        assertEquals(testTask.getPriority().getValue(), result.priority());
        assertEquals(testTask.getDifficulty().getLevel(), result.difficulty());
        verify(taskService).createTask(eq(authorId), eq(executorId), any(Task.class));
    }

    @Test
    void createTask_WithNonExistentAuthor_ThrowsEntityNotFoundException() {
        // Arrange
        Task testTask = createTestTask();
        TaskDTO testTaskDTO = TaskDTO.fromORM(testTask);
        Long authorId = 99L;
        Long executorId = 2L;
        when(taskService.createTask(eq(authorId), eq(executorId), any(Task.class)))
            .thenThrow(new EntityNotFoundException("Author not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, 
            () -> taskController.createTask(authorId, executorId, testTaskDTO));
        verify(taskService).createTask(eq(authorId), eq(executorId), any(Task.class));
    }

    @Test
    void createTask_WithNonExistentExecutor_ThrowsEntityNotFoundException() {
        // Arrange
        Task testTask = createTestTask();
        TaskDTO testTaskDTO = TaskDTO.fromORM(testTask);
        Long authorId = 1L;
        Long executorId = 99L;
        when(taskService.createTask(eq(authorId), eq(executorId), any(Task.class)))
            .thenThrow(new EntityNotFoundException("Executor not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, 
            () -> taskController.createTask(authorId, executorId, testTaskDTO));
        verify(taskService).createTask(eq(authorId), eq(executorId), any(Task.class));
    }

    @Test
    void getTasksByUser_Success() {
        // Arrange
        Task testTask = createTestTask();
        List<Task> tasks = List.of(testTask);
        Long userId = 1L;
        when(taskService.getTasksByUser(userId)).thenReturn(tasks);

        // Act
        List<TaskDTO> result = taskController.getTasksByUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testTask.getTitle(), result.get(0).title());
        assertEquals(testTask.getDescription(), result.get(0).description());
        verify(taskService).getTasksByUser(userId);
    }

    @Test
    void getTasksByUser_WhenUserNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        Long userId = 99L;
        when(taskService.getTasksByUser(userId))
            .thenThrow(new EntityNotFoundException("User not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> taskController.getTasksByUser(userId));
        verify(taskService).getTasksByUser(userId);
    }

    @Test
    void getTasksByUser_WhenNoTasks_ReturnsEmptyList() {
        // Arrange
        Long userId = 1L;
        when(taskService.getTasksByUser(userId)).thenReturn(List.of());

        // Act
        List<TaskDTO> result = taskController.getTasksByUser(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(taskService).getTasksByUser(userId);
    }

    @Test
    void updateTask_Success() {
        // Arrange
        Task testTask = createTestTask();
        TaskDTO testTaskDTO = TaskDTO.fromORM(testTask);
        Integer taskId = 1;
        Integer userId = 1;
        when(taskService.updateTask(eq(taskId), eq(userId), any(Task.class))).thenReturn(testTask);

        // Act
        TaskDTO result = taskController.updateTask(taskId, userId, testTaskDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testTask.getTitle(), result.title());
        assertEquals(testTask.getDescription(), result.description());
        verify(taskService).updateTask(eq(taskId), eq(userId), any(Task.class));
    }

    @Test
    void updateTask_WhenTaskNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        Task testTask = createTestTask();
        TaskDTO testTaskDTO = TaskDTO.fromORM(testTask);
        Integer taskId = 99;
        Integer userId = 1;
        when(taskService.updateTask(eq(taskId), eq(userId), any(Task.class)))
            .thenThrow(new EntityNotFoundException("Task not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, 
            () -> taskController.updateTask(taskId, userId, testTaskDTO));
        verify(taskService).updateTask(eq(taskId), eq(userId), any(Task.class));
    }

    @Test
    void updateTask_WithUnauthorizedUser_ThrowsEntityNotFoundException() {
        // Arrange
        Task testTask = createTestTask();
        TaskDTO testTaskDTO = TaskDTO.fromORM(testTask);
        Integer taskId = 1;
        Integer userId = 99;
        when(taskService.updateTask(eq(taskId), eq(userId), any(Task.class)))
            .thenThrow(new EntityNotFoundException("User not authorized to update this task"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, 
            () -> taskController.updateTask(taskId, userId, testTaskDTO));
        verify(taskService).updateTask(eq(taskId), eq(userId), any(Task.class));
    }

    @Test
    void deleteTask_Success() {
        // Arrange
        Integer taskId = 1;
        Integer userId = 1;
        doNothing().when(taskService).deleteTask(taskId, userId);

        // Act
        taskController.deleteTask(taskId, userId);

        // Assert
        verify(taskService).deleteTask(taskId, userId);
    }

    @Test
    void deleteTask_WhenTaskNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        Integer taskId = 99;
        Integer userId = 1;
        doThrow(new EntityNotFoundException("Task not found"))
            .when(taskService).deleteTask(taskId, userId);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, 
            () -> taskController.deleteTask(taskId, userId));
        verify(taskService).deleteTask(taskId, userId);
    }

    @Test
    void deleteTask_WithUnauthorizedUser_ThrowsEntityNotFoundException() {
        // Arrange
        Integer taskId = 1;
        Integer userId = 99;
        doThrow(new EntityNotFoundException("User not authorized to delete this task"))
            .when(taskService).deleteTask(taskId, userId);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, 
            () -> taskController.deleteTask(taskId, userId));
        verify(taskService).deleteTask(taskId, userId);
    }
} 