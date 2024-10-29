package com.despereaux.jpa_scheduler.service;

import com.despereaux.jpa_scheduler.dto.CommentRequestDto;
import com.despereaux.jpa_scheduler.dto.CommentResponseDto;
import com.despereaux.jpa_scheduler.entity.Comment;
import com.despereaux.jpa_scheduler.entity.Schedule;
import com.despereaux.jpa_scheduler.repository.CommentRepository;
import com.despereaux.jpa_scheduler.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;

    public void createComment(Long scheduleId, CommentRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다: " + scheduleId));
        Comment comment = new Comment(
                requestDto.getComment(),
                requestDto.getUsername(),
                schedule
        );
        commentRepository.save(comment);
    }

    public List<CommentResponseDto> getAllComments(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다: " + scheduleId));
        return schedule.getComments()
                .stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    public CommentResponseDto getCommentById(Long scheduleId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .filter(c -> c.getSchedule().getId().equals(scheduleId))
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + commentId));
        return new CommentResponseDto(comment);
    }

    public void updateComment(Long scheduleId, Long commentId, CommentRequestDto requestDto) {
        Comment comment = commentRepository.findById(commentId)
                .filter(c -> c.getSchedule().getId().equals(scheduleId))
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + commentId));
        comment.setUsername(requestDto.getUsername());
        comment.setComment(requestDto.getComment());
        commentRepository.save(comment);
    }

    public void deleteComment(Long scheduleId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .filter(c -> c.getSchedule().getId().equals(scheduleId))
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다: " + commentId));
        commentRepository.delete(comment);
    }
}
