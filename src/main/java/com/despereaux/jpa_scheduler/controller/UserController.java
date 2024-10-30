package com.despereaux.jpa_scheduler.controller;

import com.despereaux.jpa_scheduler.dto.UserRequestDto;
import com.despereaux.jpa_scheduler.dto.UserResponseDto;
import com.despereaux.jpa_scheduler.entity.User;
import com.despereaux.jpa_scheduler.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public UserResponseDto register(@RequestBody UserRequestDto userRequestDto) {
        User user = userService.register(userRequestDto);
        return new UserResponseDto(user, null);
    }

    @PostMapping("/login")
    public UserResponseDto login(@RequestBody UserRequestDto userRequestDto) {
        String token = userService.login(userRequestDto.getEmail(), userRequestDto.getPassword());
        User user = userService.findByEmail(userRequestDto.getEmail());
        return new UserResponseDto(user, token);
    }

    @PostMapping("/")
    public UserResponseDto createUser(@RequestBody UserRequestDto requestDto) {
        return userService.createUser(requestDto);
    }

    @GetMapping("/")
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
