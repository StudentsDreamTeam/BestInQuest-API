package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.model.Task;
import com.github.StudentsDreamTeam.repository.TaskRepository;
import com.github.StudentsDreamTeam.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Task> getTasksByUser(Long userId) {
        return taskRepository.findByUserId(userId);
//        return List.of();
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
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to delete this task.");
        }

        taskRepository.delete(task);
    }

    public Task createTask(Integer userId, Task task) {
        return taskRepository.save(task);
         }
}
