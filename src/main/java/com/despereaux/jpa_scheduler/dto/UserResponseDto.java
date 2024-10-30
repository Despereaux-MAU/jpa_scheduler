package com.despereaux.jpa_scheduler.dto;

import com.despereaux.jpa_scheduler.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserResponseDto {

    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private String role;
    private String token;

    public UserResponseDto(User user, String token) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.token = token;
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();
    }
}
