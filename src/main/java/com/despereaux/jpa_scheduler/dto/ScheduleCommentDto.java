package com.despereaux.jpa_scheduler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleCommentDto {
    private String content;
    private String username;
    private Long scheduleId;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private Long commentCount;
    private Long id;
}
