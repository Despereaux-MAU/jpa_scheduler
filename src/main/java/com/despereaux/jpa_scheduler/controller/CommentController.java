package com.despereaux.jpa_scheduler.controller;

import com.despereaux.jpa_scheduler.dto.CommentRequestDto;
import com.despereaux.jpa_scheduler.dto.CommentResponseDto;
import com.despereaux.jpa_scheduler.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules/{scheduleId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/")
    public void createComment(@PathVariable Long scheduleId, @RequestBody CommentRequestDto requestDto) {
        commentService.createComment(scheduleId, requestDto);
    }

    @GetMapping("/")
    public List<CommentResponseDto> getAllComments(@PathVariable Long scheduleId) {
        return commentService.getAllComments(scheduleId);
    }

    @GetMapping("/{commentId}")
    public CommentResponseDto getCommentById(@PathVariable Long scheduleId, @PathVariable Long commentId) {
        return commentService.getCommentById(scheduleId, commentId);
    }

    @PutMapping("/{commentId}")
    public void updateComment(
            @PathVariable Long scheduleId,
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto requestDto) {
        commentService.updateComment(scheduleId, commentId, requestDto);
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable Long scheduleId, @PathVariable Long commentId) {
        commentService.deleteComment(scheduleId, commentId);
    }
}
