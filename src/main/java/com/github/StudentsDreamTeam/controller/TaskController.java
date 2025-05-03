package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.dto.TaskDTO;
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

    @PostMapping("/create")
    public TaskDTO createTask(@RequestParam(value = "authorId") Long authorId,
                              @RequestParam(value = "executorId") Long executorId,
                              @RequestBody TaskDTO task) {
        Task task1 = taskService.createTask(authorId, executorId, Task.fromDTO(task));
        TaskDTO taskDTO = TaskDTO.fromORM(task1);
        return taskDTO;
    }

    @GetMapping("/user/{userId}")
    public List<TaskDTO> getTasksByUser(@PathVariable Long userId) {
        return taskService.getTasksByUser(userId).stream().map(TaskDTO::fromORM).toList();
    }

    @PutMapping("/{taskId}")
    public TaskDTO updateTask(@PathVariable Integer taskId, @RequestParam (value = "userID") Integer userId, @RequestBody TaskDTO task) {
        Task task1 = taskService.updateTask(taskId, userId, Task.fromDTO(task));
        TaskDTO taskDTO = TaskDTO.fromORM(task1);
        return  taskDTO;
    }

    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable Integer taskId, @RequestParam (value = "userID") Integer userId) {
        taskService.deleteTask(taskId, userId);
    }
}
