package com.spd.service;

import com.spd.model.Task;
import com.spd.repository.TaskRepository;
import com.spd.repository.UserRepository;
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
    }

    @Transactional
    public Task updateTask(Long taskId, Long userId, Task task) {
        Task existingTask = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

        if (!existingTask.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to edit this task.");
        }

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        return taskRepository.save(existingTask);
    }

    @Transactional
    public void deleteTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("You don't have permission to delete this task.");
        }

        taskRepository.delete(task);
    }
}
