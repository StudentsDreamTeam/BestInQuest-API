package com.github.StudentsDreamTeam.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskPointerTest {

    @Test
    void testTaskPointerCreation() {
        TaskPointer pointer = new TaskPointer();
        assertNotNull(pointer);
        assertNull(pointer.getId());
        assertNull(pointer.getLinkedTask());
        assertNull(pointer.getProject());
        assertNull(pointer.getCreationDate());
    }

    @Test
    void testTaskPointerWithId() {
        Integer id = 1;
        TaskPointer pointer = new TaskPointer(id);
        
        assertNotNull(pointer);
        assertNull(pointer.getId());
        assertNull(pointer.getLinkedTask());
        assertNull(pointer.getProject());
        assertNull(pointer.getCreationDate());
    }

    @Test
    void testTaskPointerWithLinkedTask() {
        // Create task
        Task task = new Task();
        task.setId(1);
        task.setTitle("Test Task");

        // Create pointer and link task
        TaskPointer pointer = new TaskPointer();
        pointer.setLinkedTask(task);

        assertNotNull(pointer.getLinkedTask());
        assertEquals(task.getId(), pointer.getLinkedTask().getId());
        assertEquals(task.getTitle(), pointer.getLinkedTask().getTitle());
    }

    @Test
    void testTaskPointerWithProject() {
        // Create project
        Project project = new Project();
        project.setId(1);
        project.setName("Test Project");

        // Create pointer and link project
        TaskPointer pointer = new TaskPointer();
        pointer.setProject(project);

        assertNotNull(pointer.getProject());
        assertEquals(project.getId(), pointer.getProject().getId());
        assertEquals(project.getName(), pointer.getProject().getName());
    }

    @Test
    void testTaskPointerWithCreationDate() {
        LocalDateTime now = LocalDateTime.now();
        
        TaskPointer pointer = new TaskPointer();
        pointer.setCreationDate(now);

        assertNotNull(pointer.getCreationDate());
        assertEquals(now, pointer.getCreationDate());
    }

    @Test
    void testTaskPointerFullSetup() {
        // Create task and project
        Task task = new Task();
        task.setId(1);
        task.setTitle("Test Task");

        Project project = new Project();
        project.setId(1);
        project.setName("Test Project");

        LocalDateTime now = LocalDateTime.now();

        // Create and setup pointer
        TaskPointer pointer = new TaskPointer();
        pointer.setId(1);
        pointer.setLinkedTask(task);
        pointer.setProject(project);
        pointer.setCreationDate(now);

        // Verify all fields
        assertEquals(1, pointer.getId());
        assertNotNull(pointer.getLinkedTask());
        assertEquals(task.getId(), pointer.getLinkedTask().getId());
        assertEquals(task.getTitle(), pointer.getLinkedTask().getTitle());
        assertNotNull(pointer.getProject());
        assertEquals(project.getId(), pointer.getProject().getId());
        assertEquals(project.getName(), pointer.getProject().getName());
        assertEquals(now, pointer.getCreationDate());
    }

    @Test
    void testTaskPointerSetters() {
        TaskPointer pointer = new TaskPointer();
        
        // Test id setter
        pointer.setId(1);
        assertEquals(1, pointer.getId());
        
        // Test linkedTask setter
        Task task = new Task();
        task.setId(1);
        pointer.setLinkedTask(task);
        assertEquals(task, pointer.getLinkedTask());
        
        // Test project setter
        Project project = new Project();
        project.setId(1);
        pointer.setProject(project);
        assertEquals(project, pointer.getProject());
        
        // Test creationDate setter
        LocalDateTime now = LocalDateTime.now();
        pointer.setCreationDate(now);
        assertEquals(now, pointer.getCreationDate());
    }

    @Test
    void testNullableFields() {
        TaskPointer taskPointer = new TaskPointer();
        
        // project и creationDate могут быть null
        taskPointer.setProject(null);
        taskPointer.setCreationDate(null);

        assertNull(taskPointer.getProject());
        assertNull(taskPointer.getCreationDate());
    }
} 