package com.despereaux.jpa_scheduler.controller;

import com.despereaux.jpa_scheduler.dto.ScheduleRequestDto;
import com.despereaux.jpa_scheduler.dto.ScheduleResponseDto;
import com.despereaux.jpa_scheduler.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 일정 생성
    @PostMapping("/")
    public void createSchedule(@RequestBody ScheduleRequestDto requestDto) {
        scheduleService.createSchedule(requestDto);
    }

    // 일정 전체 조회
    @GetMapping("/")
    public List<ScheduleResponseDto> getAllSchedules() {
        return scheduleService.getAllSchedules();
    }

    // 일정 {id} 조회
    @GetMapping("/{id}")
    public Optional<ScheduleResponseDto> getScheduleById(@PathVariable Long id) {
        return scheduleService.getScheduleById(id);
    }

    // 일정 수정
    @PutMapping("/{id}")
    public void updateSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto requestDto) {
        scheduleService.updateSchedule(id, requestDto);
    }

    // 일정 삭제
    @DeleteMapping("/{id}")
    public void deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteScheduleById(id);
    }
}
