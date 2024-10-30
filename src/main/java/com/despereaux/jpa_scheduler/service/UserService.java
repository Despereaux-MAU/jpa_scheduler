package com.despereaux.jpa_scheduler.service;

import com.despereaux.jpa_scheduler.config.PasswordEncoder;
import com.despereaux.jpa_scheduler.dto.UserRequestDto;
import com.despereaux.jpa_scheduler.dto.UserResponseDto;
import com.despereaux.jpa_scheduler.entity.User;
import com.despereaux.jpa_scheduler.entity.UserRoleEnum;
import com.despereaux.jpa_scheduler.jwt.JwtUtil;
import com.despereaux.jpa_scheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public User register(UserRequestDto userRequestDto) {
        User user = new User();
        user.setUsername(userRequestDto.getUsername());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDto.getPassword()));
        user.setRole(UserRoleEnum.USER);  // 기본 권한: USER
        return userRepository.save(user);
    }

    // 새롭게 추가된 createUser 메서드
    public UserResponseDto createUser(UserRequestDto requestDto) {
        User user = register(requestDto);  // 회원가입 로직 재사용
        return new UserResponseDto(user, null);  // 토큰 없이 생성된 사용자 반환
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("이메일 또는 비밀번호가 올바르지 않습니다."));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }
        return jwtUtil.generateToken(user.getEmail(), user.getRole().getAuthority());
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));
    }

    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponseDto(user, null))
                .collect(Collectors.toList());
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + id));
        return new UserResponseDto(user, null);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + id));
        userRepository.delete(user);
    }
}
