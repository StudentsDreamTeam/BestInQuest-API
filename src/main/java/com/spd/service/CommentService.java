package com.spd.service;

import com.spd.model.Comment;
import com.spd.repository.CommentRepository;
import com.spd.repository.TaskRepository;
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
            .orElseThrow(() -> new RuntimeException("Task not found"));

        return commentRepository.save(comment);
    }
    
    public List<Comment> getCommentsForTask(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }
}
