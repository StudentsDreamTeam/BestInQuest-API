package com.spd.controller;

import com.spd.model.Comment;
import com.spd.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/")
    public Comment addCommentToTask(@RequestBody Comment comment) {
        return commentService.addCommentToTask(comment);
    }

    @GetMapping("/task/{taskId}")
    public List<Comment> getCommentsForTask(@PathVariable Long taskId) {
        return commentService.getCommentsForTask(taskId);
    }
}
