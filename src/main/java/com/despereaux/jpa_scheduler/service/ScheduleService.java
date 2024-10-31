package com.despereaux.jpa_scheduler.service;

import com.despereaux.jpa_scheduler.dto.ScheduleRequestDto;
import com.despereaux.jpa_scheduler.dto.ScheduleResponseDto;
import com.despereaux.jpa_scheduler.entity.Schedule;
import com.despereaux.jpa_scheduler.entity.User;
import com.despereaux.jpa_scheduler.exception.AccessDeniedException;
import com.despereaux.jpa_scheduler.jwt.JwtUtil;
import com.despereaux.jpa_scheduler.repository.CommentRepository;
import com.despereaux.jpa_scheduler.repository.ScheduleRepository;
import com.despereaux.jpa_scheduler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final WeatherService weatherService;
    private final JwtUtil jwtUtil;

    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto, String token) {
        User user = authenticateUser(token, requestDto.getUserId());
        String weatherDescription = weatherService.fetchWeatherDescription();

        Schedule schedule = Schedule.create(
                requestDto.getTitle(),
                requestDto.getContent(),
                weatherDescription,
                user
        );

        Schedule savedSchedule = scheduleRepository.save(schedule);
        return new ScheduleResponseDto(savedSchedule);
    }

    public void updateSchedule(Long id, ScheduleRequestDto requestDto, String token) {
        validateAdminRole(token);

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다." + id));
        User user = userRepository.findById(requestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다." + requestDto.getUserId()));

        schedule.update(requestDto.getTitle(), requestDto.getContent(), user);
        scheduleRepository.save(schedule);
    }

    private User authenticateUser(String token, Long userId) {
        if (token != null && jwtUtil.validateToken(token)) {
            String username = jwtUtil.getUsernameFromToken(token);
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + username));
        } else {
            return userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
        }
    }

    private void validateAdminRole(String token) {
        String role = jwtUtil.getRoleFromToken(token);
        if (!"ADMIN".equals(role)) {
            throw new AccessDeniedException("관리자 권한이 필요합니다.");
        }
    }
}
