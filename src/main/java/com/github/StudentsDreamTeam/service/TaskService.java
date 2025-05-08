package com.github.StudentsDreamTeam.service;

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
        executor.setLevel(executor.getLevel() + existingTask.getRewardCurrency());
        userRepository.save(executor);

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
    }

    private void revertTaskRewardWithBonuses(Task existingTask) {
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

        long finalXp = - baseXp * xpMultiplier;
        long finalCurrency = baseCurrency * currencyMultiplier;

        executor.setXp(Math.max(0, executor.getXp() + (int) finalXp));
        executor.setLevel(Math.max(0, executor.getLevel() - existingTask.getRewardCurrency()));
        userRepository.save(executor);

        xpGainsRepository.save(new XpGains(
                executor,
                (int) finalXp,
                LocalDateTime.now()
        ));

        spendingsRepository.save(new Spendings(
                executor,
                (int) finalCurrency,
                LocalDateTime.now(), "Task completed: " + existingTask.getTitle()
        ));
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

        if (task.getStatus() != null) {
            Status oldStatus = existingTask.getStatus();
            Status newStatus = task.getStatus();

            if (Status.DONE.equals(newStatus)) {
                applyTaskRewardWithBonuses(existingTask);
            }

            if (oldStatus == Status.DONE && newStatus != Status.DONE) {
                revertTaskRewardWithBonuses(existingTask);
            }
            existingTask.setStatus(newStatus);
        }
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

        task.getTaskPointers().add(pointer);

        Task savedTask = taskRepository.save(task);

        pointer = taskPointerRepository.save(pointer);
        task.setTaskPointer(pointer);

        return savedTask;
    }
}
