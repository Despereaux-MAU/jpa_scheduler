package com.despereaux.jpa_scheduler.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private String username;
    private String comment;

    private Long scheduleId;
}
