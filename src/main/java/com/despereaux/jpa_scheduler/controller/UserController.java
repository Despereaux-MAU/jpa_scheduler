package com.despereaux.jpa_scheduler.controller;

import com.despereaux.jpa_scheduler.dto.UserRequestDto;
import com.despereaux.jpa_scheduler.dto.UserResponseDto;
import com.despereaux.jpa_scheduler.jwt.JwtUtil;
import com.despereaux.jpa_scheduler.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(@RequestBody UserRequestDto requestDto,
                                                        @RequestHeader(value = "Authorization", required = false) String token) {
        String authToken = (token != null && token.startsWith("Bearer ")) ? token.substring(7) : null;
        UserResponseDto responseDto = userService.register(requestDto, authToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody UserRequestDto requestDto) {

        String token = userService.login(requestDto.getEmail(), requestDto.getPassword());
        UserResponseDto responseDto = new UserResponseDto(userService.findByEmail(requestDto.getEmail()));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(responseDto);
    }

    @GetMapping("/info")
    public ResponseEntity<UserResponseDto> getLoggedInUser(HttpServletRequest request) {
        String token = jwtUtil.resolveToken(request);
        UserResponseDto userResponseDto = new UserResponseDto(userService.findByToken(token));
        return ResponseEntity.ok(userResponseDto);
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
