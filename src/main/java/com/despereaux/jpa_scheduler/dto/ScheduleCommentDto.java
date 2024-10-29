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
    private String username;

    public ScheduleCommentDto(
            String title,
            String content,
            Long commentCount,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt,
            String username) {
        this.title = title;
        this.content = content;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.username = username;
    }
}
