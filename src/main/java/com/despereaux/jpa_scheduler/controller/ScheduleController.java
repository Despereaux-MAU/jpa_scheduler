package com.despereaux.jpa_scheduler.controller;

import com.despereaux.jpa_scheduler.dto.ScheduleCommentDto;
import com.despereaux.jpa_scheduler.dto.ScheduleRequestDto;
import com.despereaux.jpa_scheduler.dto.ScheduleResponseDto;
import com.despereaux.jpa_scheduler.dto.WeatherResponse;
import com.despereaux.jpa_scheduler.entity.Schedule;
import com.despereaux.jpa_scheduler.jwt.JwtUtil;
import com.despereaux.jpa_scheduler.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.thymeleaf.util.StringUtils.substring;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final WeatherResponse.WeatherService weatherService;
    private JwtUtil jwtUtil;

    @Autowired
    public ScheduleController(ScheduleService scheduleService, WeatherResponse.WeatherService weatherService) {
        this.scheduleService = scheduleService;
        this.weatherService = weatherService;
    }

    @PostMapping("/")
    public Schedule createSchedule(@RequestBody Schedule schedule) {
        WeatherResponse weatherResponse = weatherService.getWeather();
        schedule.setWeather(weatherResponse.getToday().getDescription() + " " + weatherResponse.getToday().getTemperature() + "°C");
        return scheduleService.save(schedule);
    }

    @GetMapping("/")
    public Page<ScheduleCommentDto> getAllSchedules(@RequestParam(defaultValue = "0") int page) {
        return scheduleService.getAllSchedules(page);
    }

    @GetMapping("/{id}")
    public Optional<ScheduleResponseDto> getScheduleById(@PathVariable Long id) {
        return scheduleService.getScheduleById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSchedule(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id, @RequestBody ScheduleRequestDto requestDto) {

        validateAdminRole(token); // 관리자 권한 체크
        scheduleService.updateSchedule(id, requestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {

        validateAdminRole(token); // 관리자 권한 체크
        scheduleService.deleteScheduleById(id);
        return ResponseEntity.ok().build();
    }

    private void validateAdminRole(String token) {
        if (token == null || !jwtUtil.validateToken(token.substring(7))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효한 토큰이 필요합니다.");
        }

        String role = jwtUtil.getRoleFromToken(token.substring(7));
        if (!"ADMIN".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다.");
        }
    }
}
