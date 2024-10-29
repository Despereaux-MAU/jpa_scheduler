package com.despereaux.jpa_scheduler.controller;

import com.despereaux.jpa_scheduler.dto.UserRequestDto;
import com.despereaux.jpa_scheduler.dto.UserResponseDto;
import com.despereaux.jpa_scheduler.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public void registerUser(@RequestBody @Valid UserRequestDto requestDto) {
        userService.registerUser(requestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> loginUser(@RequestBody @Valid UserRequestDto requestDto) {

        String token = userService.loginUser(requestDto);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PostMapping("/")
    public void createUser(@RequestBody @Valid UserRequestDto requestDto) {
        userService.createUser(requestDto);
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
