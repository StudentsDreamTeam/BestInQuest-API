package com.github.StudentsDreamTeam.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjectTest {

    @Test
    void testProjectCreation() {
        Project project = new Project();
        assertNotNull(project);
        // Проверяем, что taskPointers инициализируется как ArrayList
        assertNotNull(project.getTaskPointers());
    }

    @Test
    void testProjectSettersAndGetters() {
        Project project = new Project();
        User owner = new User();
        LocalDateTime now = LocalDateTime.now();
        List<TaskPointer> taskPointers = new ArrayList<>();
        TaskPointer taskPointer = new TaskPointer(1);
        taskPointers.add(taskPointer);

        project.setId(1);
        project.setName("Test Project");
        project.setDescription("Test Description");
        project.setOwner(owner);
        project.setStatus("IN_PROGRESS");
        project.setCreationDate(now);
        project.setProjectItems(5L);
        project.setDone(false);
        project.setTaskPointers(taskPointers);

        assertEquals(1, project.getId());
        assertEquals("Test Project", project.getName());
        assertEquals("Test Description", project.getDescription());
        assertEquals(owner, project.getOwner());
        assertEquals("IN_PROGRESS", project.getStatus());
        assertEquals(now, project.getCreationDate());
        assertEquals(5L, project.getProjectItems());
        assertFalse(project.getDone());
        assertEquals(taskPointers, project.getTaskPointers());
        assertEquals(1, project.getTaskPointers().size());
    }

    @Test
    void testNullableFields() {
        Project project = new Project();
        
        // Все поля кроме id могут быть null
        project.setName(null);
        project.setDescription(null);
        project.setOwner(null);
        project.setStatus(null);
        project.setCreationDate(null);
        project.setProjectItems(null);
        project.setDone(null);
        // taskPointers инициализируется в конструкторе, поэтому проверяем отдельно
        project.setTaskPointers(null);

        assertNull(project.getName());
        assertNull(project.getDescription());
        assertNull(project.getOwner());
        assertNull(project.getStatus());
        assertNull(project.getCreationDate());
        assertNull(project.getProjectItems());
        assertNull(project.getDone());
        assertNull(project.getTaskPointers());
    }

    @Test
    void testTaskPointersManipulation() {
        Project project = new Project();
        TaskPointer taskPointer1 = new TaskPointer(1);
        TaskPointer taskPointer2 = new TaskPointer(2);

        // Проверяем добавление задач
        project.getTaskPointers().add(taskPointer1);
        assertEquals(1, project.getTaskPointers().size());
        assertTrue(project.getTaskPointers().contains(taskPointer1));

        project.getTaskPointers().add(taskPointer2);
        assertEquals(2, project.getTaskPointers().size());
        assertTrue(project.getTaskPointers().contains(taskPointer2));

        // Проверяем удаление задачи
        project.getTaskPointers().remove(taskPointer1);
        assertEquals(1, project.getTaskPointers().size());
        assertFalse(project.getTaskPointers().contains(taskPointer1));
        assertTrue(project.getTaskPointers().contains(taskPointer2));

        // Проверяем очистку списка задач
        project.getTaskPointers().clear();
        assertTrue(project.getTaskPointers().isEmpty());
    }
} 