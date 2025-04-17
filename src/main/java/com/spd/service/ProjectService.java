package com.spd.service;

import com.spd.model.Project;
import com.spd.repository.ProjectRepository;
import com.spd.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Project> getProjectsByUser(Long userId) {
        return projectRepository.findByOwnerId(userId);
    }

    @Transactional
    public Project createProject(Project project, Long userId) {
        project.setOwner(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not authorized")));
        return projectRepository.save(project);
    }

    public Project getProjectInfo(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));
    }
}
