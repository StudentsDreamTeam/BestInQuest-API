package com.github.StudentsDreamTeam.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    void testCommentCreation() {
        Comment comment = new Comment();
        assertNotNull(comment);
    }

    @Test
    void testCommentSettersAndGetters() {
        Comment comment = new Comment();
        Task task = new Task();
        User user = new User();
        LocalDateTime now = LocalDateTime.now();

        comment.setId(1);
        comment.setTask(task);
        comment.setUser(user);
        comment.setText("Test comment");
        comment.setCreationDate(now);

        assertEquals(1, comment.getId());
        assertEquals(task, comment.getTask());
        assertEquals(user, comment.getUser());
        assertEquals("Test comment", comment.getText());
        assertEquals(now, comment.getCreationDate());
    }

    @Test
    void testNullableFields() {
        Comment comment = new Comment();
        
        // Все поля кроме id могут быть null
        comment.setTask(null);
        comment.setUser(null);
        comment.setText(null);
        comment.setCreationDate(null);

        assertNull(comment.getTask());
        assertNull(comment.getUser());
        assertNull(comment.getText());
        assertNull(comment.getCreationDate());
    }
} 