package com.despereaux.jpa_scheduler.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class UserRequestDto {
    private String username;
    private String email;
    private String password;
    private Long userId;
}
