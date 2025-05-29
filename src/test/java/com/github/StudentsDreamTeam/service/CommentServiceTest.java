package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.model.Comment;
import com.github.StudentsDreamTeam.model.Task;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.repository.CommentRepository;
import com.github.StudentsDreamTeam.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceTest {
    
    private CommentService commentService;
    private CommentRepository commentRepository;
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        taskRepository = mock(TaskRepository.class);
        commentService = new CommentService();
        // Используем рефлексию для установки mock-объектов
        try {
            var commentRepoField = CommentService.class.getDeclaredField("commentRepository");
            var taskRepoField = CommentService.class.getDeclaredField("taskRepository");
            commentRepoField.setAccessible(true);
            taskRepoField.setAccessible(true);
            commentRepoField.set(commentService, commentRepository);
            taskRepoField.set(commentService, taskRepository);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testAddCommentToTask() {
        Task task = new Task();
        task.setId(1);
        User user = new User();
        user.setId(1);
        Comment comment = new Comment();
        comment.setTask(task);
        comment.setUser(user);
        comment.setText("Test comment");
        comment.setCreationDate(LocalDateTime.now());

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        Comment savedComment = commentService.addCommentToTask(comment);

        assertNotNull(savedComment);
        assertEquals(comment.getText(), savedComment.getText());
        assertEquals(comment.getTask(), savedComment.getTask());
        assertEquals(comment.getUser(), savedComment.getUser());
        verify(taskRepository).findById(task.getId());
        verify(commentRepository).save(comment);
    }

    @Test
    void testAddCommentToNonexistentTask() {
        Task task = new Task();
        task.setId(999);
        Comment comment = new Comment();
        comment.setTask(task);

        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> commentService.addCommentToTask(comment));
        verify(taskRepository).findById(task.getId());
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void testGetCommentsForTask() {
        Long taskId = 1L;
        Comment comment1 = new Comment();
        Comment comment2 = new Comment();
        List<Comment> comments = Arrays.asList(comment1, comment2);

        when(commentRepository.findByTaskId(taskId)).thenReturn(comments);

        List<Comment> result = commentService.getCommentsForTask(taskId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(commentRepository).findByTaskId(taskId);
    }

    @Test
    void testGetCommentsForTaskNoComments() {
        Long taskId = 1L;
        when(commentRepository.findByTaskId(taskId)).thenReturn(List.of());

        List<Comment> result = commentService.getCommentsForTask(taskId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(commentRepository).findByTaskId(taskId);
    }

    @Test
    void testCommentCreation() {
        Comment comment = new Comment();
        Task task = new Task();
        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        task.setId(1);
        user.setId(1);

        comment.setTask(task);
        comment.setUser(user);
        comment.setText("Test comment");
        comment.setCreationDate(now);

        assertNotNull(comment);
        assertEquals(task, comment.getTask());
        assertEquals(user, comment.getUser());
        assertEquals("Test comment", comment.getText());
        assertEquals(now, comment.getCreationDate());
    }

    @Test
    void testCommentWithNullTask() {
        Comment comment = new Comment();
        User user = new User();
        
        user.setId(1);
        comment.setUser(user);
        comment.setText("Test comment");
        comment.setCreationDate(LocalDateTime.now());
        comment.setTask(null);

        assertNotNull(comment);
        assertNull(comment.getTask());
        assertNotNull(comment.getUser());
        assertNotNull(comment.getText());
        assertNotNull(comment.getCreationDate());
    }

    @Test
    void testCommentWithNullUser() {
        Comment comment = new Comment();
        Task task = new Task();
        
        task.setId(1);
        comment.setTask(task);
        comment.setText("Test comment");
        comment.setCreationDate(LocalDateTime.now());
        comment.setUser(null);

        assertNotNull(comment);
        assertNotNull(comment.getTask());
        assertNull(comment.getUser());
        assertNotNull(comment.getText());
        assertNotNull(comment.getCreationDate());
    }
} 