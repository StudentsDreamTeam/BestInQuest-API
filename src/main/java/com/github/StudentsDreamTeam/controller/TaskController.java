package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.model.Task;
import com.github.StudentsDreamTeam.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/user/{userId}")
    public List<Task> getTasksByUser(@PathVariable Long userId) {
        return taskService.getTasksByUser(userId);
    }

    @PutMapping("/{taskId}")
    public Task updateTask(@PathVariable Integer taskId, @RequestParam Integer userId, @RequestBody Task task) {
        return taskService.updateTask(taskId, userId, task);
    }

    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable Integer taskId, @RequestParam Integer userId) {
        taskService.deleteTask(taskId, userId); 
    }
}
