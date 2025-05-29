package com.github.StudentsDreamTeam.task;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.github.StudentsDreamTeam.enums.Difficulty;
import com.github.StudentsDreamTeam.enums.Priority;
import com.github.StudentsDreamTeam.enums.Status;
import com.github.StudentsDreamTeam.model.Task;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.repository.*;
import com.github.StudentsDreamTeam.service.AchievementDetector;
import com.github.StudentsDreamTeam.service.TaskService;
import com.github.StudentsDreamTeam.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TaskServiceTests {

    private static final LocalDateTime now = LocalDateTime.now();
    private static final LocalDateTime yesterday = now.minusDays(1);
    private static final LocalDateTime tomorrow = now.plusDays(1);
    private static final LocalDateTime longTimeAgo = now.minusYears(2).minusDays(5).minusMonths(1);

    private static final Duration fiveMinutes = Duration.ofMinutes(5);

    @Mock
    private UserService userService;

    @Mock
    private AchievementDetector achievementDetector;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UsersInventoryRepository usersInventoryRepository;

    @Mock
    private XpGainsRepository xpGainsRepository;

    @Mock
    private IncomeRepository incomeRepository;

    @Mock
    private SpendingsRepository spendingsRepository;

    @Mock
    private TaskPointerRepository taskPointerRepository;

    @InjectMocks
    private TaskService taskService;

    public static User createSampleUser(Integer id) {
        User expected = new User();
        expected.setId(id);
        expected.setName("Juan");
        expected.setXp(120);
        expected.setLevel(1);
        expected.setLastInDate(now);
        expected.setRegistrationDate(now);
        expected.setEmail("example@example.org");
        expected.setPassword("some strong password");
        expected.setStreak(0);
        return expected;
    }

    public static Task createSampleTask(User author, User executor) {
        Task sample = new Task();
        sample.setTitle("test");
        sample.setDescription("test");
        sample.setDeadline(tomorrow);
        sample.setDuration(fiveMinutes);
        sample.setSphere("test");
        sample.setCombo(false);
        sample.setPriority(Priority.LOW);
        sample.setDifficulty(Difficulty.EASY);
        sample.setStatus(Status.NEW);
        sample.setRewardCurrency(20);
        sample.setRewardXp(100);
        sample.setFastDoneBonus(20);
        sample.setUpdateDate(now);
        sample.setAuthor(author);
        sample.setExecutor(executor);
        return sample;
    }

    @BeforeEach
    public void resetMocks() {
        reset(userService, achievementDetector, taskRepository, userRepository, usersInventoryRepository,
                xpGainsRepository, incomeRepository, spendingsRepository, taskPointerRepository);
    }

    @Test
    void taskCreationTest() {
        User author = createSampleUser(0);
        Task argument = createSampleTask(author, author);
        Task expected = createSampleTask(author, author);
        expected.setId(0);

        when(userRepository.findById(0L)).thenReturn(Optional.of(author));
        when(taskRepository.save(any())).thenReturn(expected);

        taskService.createTask(0L, 0L, argument);

        verify(userRepository, times(2)).findById(0L);
        verifyNoMoreInteractions(userRepository);
        verify(taskRepository).save(argument);
        verifyNoMoreInteractions(taskRepository);
        verify(taskPointerRepository).save(any());
        verifyNoMoreInteractions(taskPointerRepository);

    }

    @Test
    void taskInvalidCreationTest() {
        User author = createSampleUser(0);
        Task argument = createSampleTask(null, null);

        when(userRepository.findById(0L)).thenReturn(Optional.of(author));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> taskService.createTask(1L, 1L, argument));
        Assertions.assertThrows(EntityNotFoundException.class, () -> taskService.createTask(1L, 0L, argument));
        Assertions.assertThrows(EntityNotFoundException.class, () -> taskService.createTask(0L, 1L, argument));


        verify(userRepository, atLeast(3)).findById(1L);
        verify(userRepository, atMost(4)).findById(1L);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(taskRepository);
        verifyNoInteractions(taskPointerRepository);

    }

    @Test
    void taskUpdateTest() {
        User author = createSampleUser(0);
        User newExecutor = createSampleUser(1);

        Task updated = createSampleTask(author, author);
        updated.setExecutor(newExecutor);
        updated.setTitle("Updated");
        updated.setDuration(fiveMinutes.plusHours(1));
        updated.setDescription("updated");
        updated.setRewardXp(0);
        updated.setRewardCurrency(0);
        updated.setFastDoneBonus(0);
        updated.setDifficulty(Difficulty.HARD);
        updated.setStatus(Status.WAITING_REVIEW);

        Task expected = createSampleTask(author, author);
        expected.setExecutor(newExecutor);
        expected.setTitle("Updated");
        expected.setDuration(fiveMinutes.plusHours(1));
        expected.setDescription("updated");
        expected.setRewardXp(0);
        expected.setRewardCurrency(0);
        expected.setFastDoneBonus(0);
        expected.setDifficulty(Difficulty.HARD);
        expected.setStatus(Status.WAITING_REVIEW);
        expected.setUpdateDate(now);


        Task old = createSampleTask(author, author);
        old.setUpdateDate(longTimeAgo);

        when(taskRepository.findById(0)).thenReturn(Optional.of(old));
        when(userRepository.findById(1L)).thenReturn(Optional.of(newExecutor));
        when(taskRepository.save(any())).thenReturn(expected);

        Task actual = taskService.updateTask(0, 0, updated);

        verify(taskRepository).findById(0);

        verify(userRepository).findById(1L);
        verifyNoMoreInteractions(userRepository);

        verify(taskRepository).save(any());
        verifyNoMoreInteractions(taskRepository);

    }


}
