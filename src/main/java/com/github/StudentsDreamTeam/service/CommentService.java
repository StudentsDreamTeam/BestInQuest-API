package com.github.StudentsDreamTeam.service;

import com.github.StudentsDreamTeam.model.Comment;
import com.github.StudentsDreamTeam.repository.CommentRepository;
import com.github.StudentsDreamTeam.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Transactional
    public Comment addCommentToTask(Comment comment) {
        taskRepository.findById(comment.getTask().getId())
            .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        return commentRepository.save(comment);
    }
    
    public List<Comment> getCommentsForTask(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }
}
