package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.enums.Status;
import com.github.StudentsDreamTeam.model.*;
import com.github.StudentsDreamTeam.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    private TaskService taskService;
    private UserService userService;
    private AchievementDetector achievementDetector;
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private UsersInventoryRepository usersInventoryRepository;
    private XpGainsRepository xpGainsRepository;
    private IncomeRepository incomeRepository;
    private SpendingsRepository spendingsRepository;
    private TaskPointerRepository taskPointerRepository;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        achievementDetector = mock(AchievementDetector.class);
        taskRepository = mock(TaskRepository.class);
        userRepository = mock(UserRepository.class);
        usersInventoryRepository = mock(UsersInventoryRepository.class);
        xpGainsRepository = mock(XpGainsRepository.class);
        incomeRepository = mock(IncomeRepository.class);
        spendingsRepository = mock(SpendingsRepository.class);
        taskPointerRepository = mock(TaskPointerRepository.class);
        
        taskService = new TaskService();
        
        // Используем рефлексию для установки mock-объектов
        try {
            var userServiceField = TaskService.class.getDeclaredField("userService");
            var achievementDetectorField = TaskService.class.getDeclaredField("achievementDetector");
            var taskRepoField = TaskService.class.getDeclaredField("taskRepository");
            var userRepoField = TaskService.class.getDeclaredField("userRepository");
            var usersInventoryRepoField = TaskService.class.getDeclaredField("usersInventoryRepository");
            var xpGainsRepoField = TaskService.class.getDeclaredField("xpGainsRepository");
            var incomeRepoField = TaskService.class.getDeclaredField("incomeRepository");
            var spendingsRepoField = TaskService.class.getDeclaredField("spendingsRepository");
            var taskPointerRepoField = TaskService.class.getDeclaredField("taskPointerRepository");
            
            userServiceField.setAccessible(true);
            achievementDetectorField.setAccessible(true);
            taskRepoField.setAccessible(true);
            userRepoField.setAccessible(true);
            usersInventoryRepoField.setAccessible(true);
            xpGainsRepoField.setAccessible(true);
            incomeRepoField.setAccessible(true);
            spendingsRepoField.setAccessible(true);
            taskPointerRepoField.setAccessible(true);
            
            userServiceField.set(taskService, userService);
            achievementDetectorField.set(taskService, achievementDetector);
            taskRepoField.set(taskService, taskRepository);
            userRepoField.set(taskService, userRepository);
            usersInventoryRepoField.set(taskService, usersInventoryRepository);
            xpGainsRepoField.set(taskService, xpGainsRepository);
            incomeRepoField.set(taskService, incomeRepository);
            spendingsRepoField.set(taskService, spendingsRepository);
            taskPointerRepoField.set(taskService, taskPointerRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetTasksByUser() {
        // Подготовка данных
        Long userId = 1L;
        Task task1 = new Task();
        task1.setId(1);
        Task task2 = new Task();
        task2.setId(2);
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskRepository.findByAuthorIdOrExecutorId(userId, userId)).thenReturn(tasks);

        // Выполнение
        List<Task> result = taskService.getTasksByUser(userId);

        // Проверки
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(task1.getId(), result.get(0).getId());
        assertEquals(task2.getId(), result.get(1).getId());
        
        verify(taskRepository).findByAuthorIdOrExecutorId(userId, userId);
    }

    @Test
    void testCreateTask() {
        // Подготовка данных
        Long authorId = 1L;
        Long executorId = 2L;
        
        User author = new User();
        author.setId(1);
        User executor = new User();
        executor.setId(2);
        
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        
        when(userRepository.findById(authorId)).thenReturn(Optional.of(author));
        when(userRepository.findById(executorId)).thenReturn(Optional.of(executor));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task savedTask = invocation.getArgument(0);
            savedTask.setId(1);
            return savedTask;
        });
        when(taskPointerRepository.save(any(TaskPointer.class))).thenAnswer(invocation -> {
            TaskPointer savedPointer = invocation.getArgument(0);
            savedPointer.setId(1);
            return savedPointer;
        });

        // Выполнение
        Task result = taskService.createTask(authorId, executorId, task);

        // Проверки
        assertNotNull(result);
        assertEquals(task.getTitle(), result.getTitle());
        assertEquals(task.getDescription(), result.getDescription());
        assertEquals(author, result.getAuthor());
        assertEquals(executor, result.getExecutor());
        assertNotNull(result.getUpdateDate());
        assertNotNull(result.getTaskPointer());
        
        verify(userRepository).findById(authorId);
        verify(userRepository).findById(executorId);
        verify(taskRepository).save(task);
        verify(taskPointerRepository).save(any(TaskPointer.class));
    }

    @Test
    void testCreateTaskWithInvalidAuthor() {
        // Подготовка данных
        Long authorId = 999L;
        Long executorId = 1L;
        
        User executor = new User();
        executor.setId(1);
        
        Task task = new Task();
        task.setTitle("Test Task");
        
        when(userRepository.findById(authorId)).thenReturn(Optional.empty());
        when(userRepository.findById(executorId)).thenReturn(Optional.of(executor));

        // Проверка исключения
        assertThrows(EntityNotFoundException.class, () -> taskService.createTask(authorId, executorId, task));
        
        verify(userRepository).findById(authorId);
        verify(userRepository, never()).findById(executorId);
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskPointerRepository, never()).save(any(TaskPointer.class));
    }

    @Test
    void testUpdateTaskWithStatusChange() {
        // Подготовка данных
        Integer taskId = 1;
        Integer userId = 1;
        
        User author = new User();
        author.setId(userId);
        
        User executor = new User();
        executor.setId(2);
        executor.setXp(100);
        
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setAuthor(author);
        existingTask.setExecutor(executor);
        existingTask.setStatus(Status.IN_PROGRESS);
        existingTask.setRewardXp(50);
        existingTask.setRewardCurrency(100);
        existingTask.setTitle("Old Title");
        existingTask.setAppliedXpReward(0L);
        existingTask.setAppliedCurrencyReward(0L);
        
        Task updatedTask = new Task();
        updatedTask.setTitle("New Title");
        updatedTask.setStatus(Status.DONE);
        updatedTask.setRewardXp(50);
        updatedTask.setRewardCurrency(100);
        
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(usersInventoryRepository.findByUserId(executor.getId())).thenReturn(List.of());
        when(userRepository.save(any(User.class))).thenReturn(executor);
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);
        when(achievementDetector.detectForUser(any(User.class))).thenReturn(List.of());

        // Выполнение
        Task result = taskService.updateTask(taskId, userId, updatedTask);

        // Проверки
        assertNotNull(result);
        assertEquals(updatedTask.getTitle(), result.getTitle());
        assertEquals(Status.DONE, result.getStatus());
        assertEquals(50L, result.getAppliedXpReward());
        assertEquals(100L, result.getAppliedCurrencyReward());
        
        verify(taskRepository).findById(taskId);
        verify(usersInventoryRepository).findByUserId(executor.getId());
        verify(userRepository).save(executor);
        verify(userService).updateUserLevel(executor);
        verify(achievementDetector).detectForUser(executor);
        verify(xpGainsRepository).save(any(XpGains.class));
        verify(incomeRepository).save(any(Income.class));
        verify(taskRepository).save(existingTask);
    }

    @Test
    void testUpdateTaskWithoutPermission() {
        // Подготовка данных
        Integer taskId = 1;
        Integer userId = 2; // Другой пользователь
        
        User author = new User();
        author.setId(1);
        
        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setAuthor(author);
        
        Task updatedTask = new Task();
        updatedTask.setTitle("New Title");
        
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));

        // Проверка исключения
        assertThrows(SecurityException.class, () -> taskService.updateTask(taskId, userId, updatedTask));
        
        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void testDeleteTask() {
        // Подготовка данных
        Integer taskId = 1;
        Integer userId = 1;
        
        User author = new User();
        author.setId(userId);
        
        Task task = new Task();
        task.setId(taskId);
        task.setAuthor(author);
        
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Выполнение
        taskService.deleteTask(taskId, userId);

        // Проверки
        verify(taskRepository).findById(taskId);
        verify(taskPointerRepository).deleteAllByLinkedTask(task);
        verify(taskRepository).delete(task);
    }

    @Test
    void testDeleteTaskWithoutPermission() {
        // Подготовка данных
        Integer taskId = 1;
        Integer userId = 2; // Другой пользователь
        
        User author = new User();
        author.setId(1);
        
        Task task = new Task();
        task.setId(taskId);
        task.setAuthor(author);
        
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Проверка исключения
        assertThrows(SecurityException.class, () -> taskService.deleteTask(taskId, userId));
        
        verify(taskRepository).findById(taskId);
        verify(taskPointerRepository, never()).deleteAllByLinkedTask(any(Task.class));
        verify(taskRepository, never()).delete(any(Task.class));
    }
} 