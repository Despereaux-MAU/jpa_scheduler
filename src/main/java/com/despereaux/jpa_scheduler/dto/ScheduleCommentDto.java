package com.despereaux.jpa_scheduler.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleCommentDto {

    private String title;
    private String content;
    private Long commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long userId;

    public ScheduleCommentDto(
            String title,
            String content,
            Long commentCount,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt,
            Long userId) {
        this.title = title;
        this.content = content;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.userId = userId;
    }
}
