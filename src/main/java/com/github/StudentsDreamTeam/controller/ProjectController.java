package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.model.Project;
import com.github.StudentsDreamTeam.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

//    @Autowired
//    private ProjectService projectService;

    private final ProjectService projectService;

    @GetMapping("/user/{userId}")
    public List<Project> getProjectsByUser(@PathVariable Long userId) {
        return projectService.getProjectsByUser(userId);
    }
}
