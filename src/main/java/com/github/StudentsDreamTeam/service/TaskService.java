package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.model.Task;
import com.github.StudentsDreamTeam.model.TaskPointer;
import com.github.StudentsDreamTeam.repository.TaskRepository;
import com.github.StudentsDreamTeam.repository.TaskPointerRepository;
import com.github.StudentsDreamTeam.repository.UserRepository;
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
    private TaskPointerRepository taskPointerRepository;

    public List<Task> getTasksByUser(Long userId) {
        return taskRepository.findByAuthorIdOrExecutorId(userId, userId);
    }

    @Transactional
    public Task updateTask(Integer taskId, Integer userId, Task task) {
        Task existingTask = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

        if (!existingTask.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to edit this task.");
        }

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        return taskRepository.save(existingTask);
    }

    @Transactional
    public void deleteTask(Integer taskId, Integer userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to delete this task.");
        }

        taskPointerRepository.deleteAllByLinkedTask(task);

        taskRepository.delete(task);
    }

    @Transactional
    public Task createTask(Long authorId, Long executorId, Task task) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Author not found"));

        User executor = userRepository.findById(executorId)
                .orElseThrow(() -> new RuntimeException("Executor not found"));

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



