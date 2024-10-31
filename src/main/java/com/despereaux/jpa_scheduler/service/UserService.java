package com.despereaux.jpa_scheduler.service;

import com.despereaux.jpa_scheduler.config.PasswordEncoder;
import com.despereaux.jpa_scheduler.dto.UserRequestDto;
import com.despereaux.jpa_scheduler.dto.UserResponseDto;
import com.despereaux.jpa_scheduler.entity.User;
import com.despereaux.jpa_scheduler.entity.UserRoleEnum;
import com.despereaux.jpa_scheduler.exception.InvalidCredentialsException;
import com.despereaux.jpa_scheduler.jwt.JwtUtil;
import com.despereaux.jpa_scheduler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Value("${admin.token}")
    private String adminToken;

    public UserResponseDto register(UserRequestDto requestDto, String token) {
        User user = User.createUser(
                requestDto.getUsername(),
                requestDto.getEmail(),
                passwordEncoder.encode(requestDto.getPassword()),
                UserRoleEnum.USER
        );

        if (adminToken.equals(token)) {
            user.assignRole(UserRoleEnum.ADMIN);
        }

        User savedUser = userRepository.save(user);
        return new UserResponseDto(savedUser);
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!user.checkPassword(password, passwordEncoder)) {
            throw new InvalidCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return jwtUtil.generateToken(user.getUsername(), user.getRole().getAuthority());
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + id));
        return new UserResponseDto(user);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + id));
        userRepository.delete(user);
    }

    public User findByToken(String token) {
        String username = jwtUtil.getUsernameFromToken(token);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }
}
