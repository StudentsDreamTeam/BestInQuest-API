package com.github.StudentsDreamTeam.controller;

import com.github.StudentsDreamTeam.model.Comment;
import com.github.StudentsDreamTeam.model.Task;
import com.github.StudentsDreamTeam.model.User;
import com.github.StudentsDreamTeam.service.CommentService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentControllerTest {

    @Mock
    private CommentService commentService;

    @InjectMocks
    private CommentController commentController;

    private Comment createTestComment() {
        User user = new User();
        user.setId(1);
        user.setName("Test User");
        user.setEmail("user@example.com");

        Task task = new Task();
        task.setId(1);
        task.setTitle("Test Task");
        task.setDescription("Test Description");

        Comment comment = new Comment();
        comment.setId(1);
        comment.setText("Test Comment Text");
        comment.setUser(user);
        comment.setTask(task);
        comment.setCreationDate(LocalDateTime.now());

        return comment;
    }

    @Test
    void addCommentToTask_Success() {
        // Arrange
        Comment testComment = createTestComment();
        when(commentService.addCommentToTask(any(Comment.class))).thenReturn(testComment);

        // Act
        Comment result = commentController.addCommentToTask(testComment);

        // Assert
        assertNotNull(result);
        assertEquals(testComment.getId(), result.getId());
        assertEquals(testComment.getText(), result.getText());
        assertEquals(testComment.getUser().getId(), result.getUser().getId());
        assertEquals(testComment.getTask().getId(), result.getTask().getId());
        verify(commentService).addCommentToTask(any(Comment.class));
    }

    @Test
    void addCommentToTask_WhenTaskNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        Comment testComment = createTestComment();
        when(commentService.addCommentToTask(any(Comment.class)))
            .thenThrow(new EntityNotFoundException("Task not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> commentController.addCommentToTask(testComment));
        verify(commentService).addCommentToTask(any(Comment.class));
    }

    @Test
    void getCommentsForTask_Success() {
        // Arrange
        Comment testComment = createTestComment();
        List<Comment> comments = List.of(testComment);
        Long taskId = 1L;
        when(commentService.getCommentsForTask(taskId)).thenReturn(comments);

        // Act
        List<Comment> result = commentController.getCommentsForTask(taskId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testComment.getId(), result.get(0).getId());
        assertEquals(testComment.getText(), result.get(0).getText());
        assertEquals(testComment.getUser().getId(), result.get(0).getUser().getId());
        assertEquals(testComment.getTask().getId(), result.get(0).getTask().getId());
        verify(commentService).getCommentsForTask(taskId);
    }

    @Test
    void getCommentsForTask_WhenTaskNotFound_ThrowsEntityNotFoundException() {
        // Arrange
        Long taskId = 99L;
        when(commentService.getCommentsForTask(taskId))
            .thenThrow(new EntityNotFoundException("Task not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> commentController.getCommentsForTask(taskId));
        verify(commentService).getCommentsForTask(taskId);
    }

    @Test
    void getCommentsForTask_ReturnsEmptyList() {
        // Arrange
        Long taskId = 1L;
        when(commentService.getCommentsForTask(taskId)).thenReturn(List.of());

        // Act
        List<Comment> result = commentController.getCommentsForTask(taskId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(commentService).getCommentsForTask(taskId);
    }
} 