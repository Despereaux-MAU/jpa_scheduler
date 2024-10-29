package com.despereaux.jpa_scheduler.service;

import com.despereaux.jpa_scheduler.dto.ScheduleRequestDto;
import com.despereaux.jpa_scheduler.dto.ScheduleResponseDto;
import com.despereaux.jpa_scheduler.entity.Schedule;
import com.despereaux.jpa_scheduler.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    // 일정 생성
    public void createSchedule(ScheduleRequestDto requestDto) {
        Schedule schedule = new Schedule(
                requestDto.getUsername(),
                requestDto.getTitle(),
                requestDto.getContent());
        scheduleRepository.save(schedule);
    }
    // 일정 전체 조회
    public List<ScheduleResponseDto> getAllSchedules() {
        return scheduleRepository
                .findAll()
                .stream()
                .map(ScheduleResponseDto::new)
                .collect(Collectors.toList());
    }

    // 일정 {id} 조회
    public Optional<ScheduleResponseDto> getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .map(ScheduleResponseDto::new);
    }

    // 일정 수정
    public void updateSchedule(Long id, ScheduleRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다." + id));
        schedule.setUsername(requestDto.getUsername());
        schedule.setTitle(requestDto.getTitle());
        schedule.setContent(requestDto.getContent());
        scheduleRepository.save(schedule);
    }

    // 일정 삭제
    public void deleteScheduleById(Long id) {
        scheduleRepository.deleteById(id);
    }
}
