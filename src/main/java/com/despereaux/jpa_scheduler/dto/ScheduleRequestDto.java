package com.despereaux.jpa_scheduler.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleRequestDto {
    private String title;
    private String content;
    private Long userId;
}
