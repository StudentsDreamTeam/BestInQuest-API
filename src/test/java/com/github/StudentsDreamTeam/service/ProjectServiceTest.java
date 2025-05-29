package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.model.Project;
import com.github.StudentsDreamTeam.model.TaskPointer;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.repository.ProjectRepository;
import com.github.StudentsDreamTeam.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    private ProjectService projectService;
    private ProjectRepository projectRepository;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        projectRepository = mock(ProjectRepository.class);
        userRepository = mock(UserRepository.class);
        projectService = new ProjectService();
        
        // Используем рефлексию для установки mock-объектов
        try {
            var projectRepoField = ProjectService.class.getDeclaredField("projectRepository");
            var userRepoField = ProjectService.class.getDeclaredField("userRepository");
            
            projectRepoField.setAccessible(true);
            userRepoField.setAccessible(true);
            
            projectRepoField.set(projectService, projectRepository);
            userRepoField.set(projectService, userRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetProjectsByUser() {
        // Подготовка данных
        Long userId = 1L;
        Project project1 = new Project();
        project1.setId(1);
        project1.setName("Project 1");
        
        Project project2 = new Project();
        project2.setId(2);
        project2.setName("Project 2");
        
        List<Project> projects = Arrays.asList(project1, project2);
        
        when(projectRepository.findByOwnerId(userId)).thenReturn(projects);

        // Выполнение
        List<Project> result = projectService.getProjectsByUser(userId);

        // Проверки
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(project1.getId(), result.get(0).getId());
        assertEquals(project2.getId(), result.get(1).getId());
        
        verify(projectRepository).findByOwnerId(userId);
    }

    @Test
    void testCreateProject() {
        // Подготовка данных
        Long userId = 1L;
        User owner = new User();
        owner.setId(1);
        
        Project project = new Project();
        project.setName("New Project");
        project.setDescription("Test Description");
        project.setStatus("NEW");
        project.setCreationDate(LocalDateTime.now());
        project.setProjectItems(0L);
        project.setDone(false);
        
        when(userRepository.findById(userId)).thenReturn(Optional.of(owner));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Выполнение
        Project result = projectService.createProject(project, userId);

        // Проверки
        assertNotNull(result);
        assertEquals(project.getName(), result.getName());
        assertEquals(project.getDescription(), result.getDescription());
        assertEquals(owner, result.getOwner());
        
        verify(userRepository).findById(userId);
        verify(projectRepository).save(project);
    }

    @Test
    void testCreateProjectWithInvalidUser() {
        // Подготовка данных
        Long userId = 999L;
        Project project = new Project();
        project.setName("New Project");
        
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Проверка исключения
        assertThrows(EntityNotFoundException.class, () -> projectService.createProject(project, userId));
        
        verify(userRepository).findById(userId);
        verify(projectRepository, never()).save(any(Project.class));
    }

    @Test
    void testGetProjectInfo() {
        // Подготовка данных
        Long projectId = 1L;
        Project project = new Project();
        project.setId(1);
        project.setName("Test Project");
        
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        // Выполнение
        Project result = projectService.getProjectInfo(projectId);

        // Проверки
        assertNotNull(result);
        assertEquals(project.getId(), result.getId());
        assertEquals(project.getName(), result.getName());
        
        verify(projectRepository).findById(projectId);
    }

    @Test
    void testGetProjectInfoNotFound() {
        // Подготовка данных
        Long projectId = 999L;
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        // Проверка исключения
        assertThrows(EntityNotFoundException.class, () -> projectService.getProjectInfo(projectId));
        
        verify(projectRepository).findById(projectId);
    }

    @Test
    void testProjectCreation() {
        Project project = new Project();
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setStatus("NEW");
        project.setCreationDate(LocalDateTime.now());
        project.setProjectItems(0L);
        project.setDone(false);
        project.setTaskPointers(new ArrayList<>());

        assertNotNull(project);
        assertEquals("Test Project", project.getName());
        assertEquals("Test Description", project.getDescription());
        assertEquals("NEW", project.getStatus());
        assertNotNull(project.getCreationDate());
        assertEquals(0L, project.getProjectItems());
        assertFalse(project.getDone());
        assertTrue(project.getTaskPointers().isEmpty());
    }

    @Test
    void testProjectWithOwner() {
        // Create owner
        User owner = new User();
        owner.setId(1);
        owner.setName("Test User");

        // Create project
        Project project = new Project();
        project.setName("Test Project");
        project.setOwner(owner);

        assertNotNull(project.getOwner());
        assertEquals(owner.getId(), project.getOwner().getId());
        assertEquals(owner.getName(), project.getOwner().getName());
    }

    @Test
    void testProjectWithTaskPointers() {
        // Create project
        Project project = new Project();
        project.setName("Test Project");

        // Create task pointers
        TaskPointer pointer1 = new TaskPointer();
        pointer1.setId(1);
        pointer1.setProject(project);

        TaskPointer pointer2 = new TaskPointer();
        pointer2.setId(2);
        pointer2.setProject(project);

        // Add task pointers to project
        ArrayList<TaskPointer> taskPointers = new ArrayList<>();
        taskPointers.add(pointer1);
        taskPointers.add(pointer2);
        project.setTaskPointers(taskPointers);

        assertEquals(2, project.getTaskPointers().size());
        assertEquals(1, project.getTaskPointers().get(0).getId());
        assertEquals(2, project.getTaskPointers().get(1).getId());
        assertSame(project, project.getTaskPointers().get(0).getProject());
        assertSame(project, project.getTaskPointers().get(1).getProject());
    }

    @Test
    void testProjectStatus() {
        Project project = new Project();
        
        // Test initial state
        project.setStatus("NEW");
        assertEquals("NEW", project.getStatus());
        
        // Test status change
        project.setStatus("IN_PROGRESS");
        assertEquals("IN_PROGRESS", project.getStatus());
        
        // Test completion
        project.setStatus("COMPLETED");
        assertEquals("COMPLETED", project.getStatus());
        project.setDone(true);
        assertTrue(project.getDone());
    }

    @Test
    void testProjectItems() {
        Project project = new Project();
        
        // Test initial state
        project.setProjectItems(0L);
        assertEquals(0L, project.getProjectItems());
        
        // Test adding items
        project.setProjectItems(5L);
        assertEquals(5L, project.getProjectItems());
    }

    @Test
    void testNullableFields() {
        Project project = new Project();
        project.setName("Test Project"); // name is required

        // These fields can be null
        assertNull(project.getId());
        assertNull(project.getDescription());
        assertNull(project.getOwner());
        assertNull(project.getStatus());
        assertNull(project.getCreationDate());
        assertNull(project.getProjectItems());
        assertNull(project.getDone());
        
        // TaskPointers list is initialized as empty by default
        assertNotNull(project.getTaskPointers());
        assertTrue(project.getTaskPointers().isEmpty());
    }
} 