package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.model.Project;
import com.github.StudentsDreamTeam.repository.ProjectRepository;
import com.github.StudentsDreamTeam.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
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
        project.setOwner(userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User designated to project owner does not exist.")));
        return projectRepository.save(project);
    }

    public Project getProjectInfo(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("Project not found"));
    }
}
