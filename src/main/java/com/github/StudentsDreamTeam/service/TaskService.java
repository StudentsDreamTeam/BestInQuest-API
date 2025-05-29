package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.dto.UserAchievementDTO;
import com.github.StudentsDreamTeam.enums.Status;
import com.github.StudentsDreamTeam.model.*;
import com.github.StudentsDreamTeam.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private UserService userService;

    @Autowired
    private AchievementDetector achievementDetector;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UsersInventoryRepository usersInventoryRepository;

    @Autowired
    private XpGainsRepository xpGainsRepository;

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private SpendingsRepository spendingsRepository;

    @Autowired
    private TaskPointerRepository taskPointerRepository;

    public List<Task> getTasksByUser(Long userId) {
        return taskRepository.findByAuthorIdOrExecutorId(userId, userId);
    }

    private void applyTaskRewardWithBonuses(Task existingTask) {
        existingTask.setUpdateDate(LocalDateTime.now());
        User executor = existingTask.getExecutor();
        List<UsersInventory> inventory = usersInventoryRepository.findByUserId(executor.getId());

        long xpMultiplier = 1;
        long currencyMultiplier = 1;

        for (UsersInventory item : inventory) {
            xpMultiplier *= item.getItem().getXpMultiplier();
            currencyMultiplier *= item.getItem().getCurrencyMultiplier();
        }

        long baseXp = existingTask.getRewardXp();
        long baseCurrency = existingTask.getRewardCurrency();

        long finalXp = baseXp * xpMultiplier;
        long finalCurrency = baseCurrency * currencyMultiplier;

        executor.setXp(executor.getXp() + (int) finalXp);
        userRepository.save(executor);
        List<UserAchievementDTO> newAchievements = achievementDetector.detectForUser(executor);

        userService.updateUserLevel(executor);

        xpGainsRepository.save(new XpGains(
                executor,
                (int) finalXp,
                LocalDateTime.now()
        ));

        incomeRepository.save(new Income(
                executor,
                (int) finalCurrency,
                LocalDateTime.now(), "Task completed: " + existingTask.getTitle()
        ));

        existingTask.setAppliedXpReward(finalXp);
        existingTask.setAppliedCurrencyReward(finalCurrency);
    }

    private void revertTaskRewardWithBonuses(Task existingTask) {
        User executor = existingTask.getExecutor();

        long finalXp = existingTask.getAppliedXpReward() != null
                ? -existingTask.getAppliedXpReward()
                : 0;
        long finalCurrency = existingTask.getAppliedCurrencyReward() != null
                ? existingTask.getAppliedCurrencyReward()
                : 0;

        executor.setXp(Math.max(0, executor.getXp() + (int) finalXp));
        userRepository.save(executor);
        achievementDetector.revertAchievementsForUser(executor);

        userService.updateUserLevel(executor);

        xpGainsRepository.save(new XpGains(
                executor,
                (int) finalXp,
                LocalDateTime.now()
        ));

        spendingsRepository.save(new Spendings(
                executor,
                (int) finalCurrency,
                LocalDateTime.now(), "Task reverted: " + existingTask.getTitle()
        ));

        existingTask.setAppliedXpReward(0L);
        existingTask.setAppliedCurrencyReward(0L);
    }

    @Transactional
    public Task updateTask(Integer taskId, Integer userId, Task task) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (!existingTask.getAuthor().getId().equals(userId)) {
            throw new SecurityException("You don't have permission to edit this task.");
        }

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setDeadline(task.getDeadline());
        existingTask.setPriority(task.getPriority());
        existingTask.setDifficulty(task.getDifficulty());
        existingTask.setRewardXp(task.getRewardXp());
        existingTask.setRewardCurrency(task.getRewardCurrency());
        existingTask.setSphere(task.getSphere());
        existingTask.setDuration(task.getDuration());
        existingTask.setFastDoneBonus(task.getFastDoneBonus());
        existingTask.setCombo(task.getCombo());

        if (task.getExecutor() != null
                && task.getExecutor().getId() != null
                && !task.getExecutor().getId().equals(existingTask.getExecutor().getId())) {

            User newExecutor = userRepository.findById(Long.valueOf(task.getExecutor().getId()))
                    .orElseThrow(() -> new EntityNotFoundException("Executor not found"));
            existingTask.setExecutor(newExecutor);
        }

        if (task.getStatus() != null) {
            Status oldStatus = existingTask.getStatus();
            Status newStatus = task.getStatus();

            if (oldStatus != Status.DONE && Status.DONE.equals(newStatus)) {
                applyTaskRewardWithBonuses(existingTask);
            }

            if (oldStatus == Status.DONE && newStatus != Status.DONE) {
                revertTaskRewardWithBonuses(existingTask);
            }
            existingTask.setStatus(newStatus);
        }
        existingTask.setUpdateDate(LocalDateTime.now());

        return taskRepository.save(existingTask);
    }

    @Transactional
    public void deleteTask(Integer taskId, Integer userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (!task.getAuthor().getId().equals(userId)) {
            throw new SecurityException("You don't have permission to delete this task.");
        }

        taskPointerRepository.deleteAllByLinkedTask(task);

        taskRepository.delete(task);
    }

    @Transactional
    public Task createTask(Long authorId, Long executorId, Task task) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author not found"));

        User executor = userRepository.findById(executorId)
                .orElseThrow(() -> new EntityNotFoundException("Executor not found"));

        task.setAuthor(author);
        task.setExecutor(executor);
        task.setUpdateDate(LocalDateTime.now());

        TaskPointer pointer = new TaskPointer();
        pointer.setCreationDate(LocalDateTime.now());
        pointer.setLinkedTask(task);

        Task savedTask = taskRepository.save(task);

        pointer = taskPointerRepository.save(pointer);
        task.setTaskPointer(pointer);

        return savedTask;
    }
}
