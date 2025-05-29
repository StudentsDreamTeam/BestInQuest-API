package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.model.Project;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.service.ProjectService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    private Project createTestProject() {
        User owner = new User();
        owner.setId(1);
        owner.setName("Test Owner");
        owner.setEmail("owner@example.com");

        Project project = new Project();
        project.setId(1);
        project.setName("Test Project");
        project.setDescription("Test Project Description");
        project.setOwner(owner);
        project.setStatus("ACTIVE");
        project.setCreationDate(LocalDateTime.now());
        project.setProjectItems(0L);
        project.setDone(false);

        return project;
    }

    @Test
    void getProjectsByUser_Success() {
        // Arrange
        Project testProject = createTestProject();
        List<Project> projects = List.of(testProject);
        Long userId = 1L;
        when(projectService.getProjectsByUser(userId)).thenReturn(projects);

        // Act
        List<Project> result = projectController.getProjectsByUser(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProject.getId(), result.get(0).getId());
        assertEquals(testProject.getName(), result.get(0).getName());
        assertEquals(testProject.getDescription(), result.get(0).getDescription());
        assertEquals(testProject.getOwner().getId(), result.get(0).getOwner().getId());
        assertEquals(testProject.getStatus(), result.get(0).getStatus());
        verify(projectService).getProjectsByUser(userId);
    }

    @Test
    void getProjectsByUser_WhenUserNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        Long userId = 99L;
        when(projectService.getProjectsByUser(userId))
            .thenThrow(new EntityNotFoundException("User not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> projectController.getProjectsByUser(userId));
        verify(projectService).getProjectsByUser(userId);
    }

    @Test
    void getProjectsByUser_WhenNoProjects_ReturnsEmptyList() {
        // Arrange
        Long userId = 1L;
        when(projectService.getProjectsByUser(userId)).thenReturn(List.of());

        // Act
        List<Project> result = projectController.getProjectsByUser(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(projectService).getProjectsByUser(userId);
    }
} 