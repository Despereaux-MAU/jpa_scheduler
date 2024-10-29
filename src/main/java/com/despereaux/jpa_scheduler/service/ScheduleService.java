package com.despereaux.jpa_scheduler.service;

import com.despereaux.jpa_scheduler.dto.ScheduleCommentDto;
import com.despereaux.jpa_scheduler.dto.ScheduleRequestDto;
import com.despereaux.jpa_scheduler.dto.ScheduleResponseDto;
import com.despereaux.jpa_scheduler.entity.Schedule;
import com.despereaux.jpa_scheduler.repository.CommentRepository;
import com.despereaux.jpa_scheduler.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final CommentRepository commentRepository;

    public void createSchedule(ScheduleRequestDto requestDto) {
        Schedule schedule = new Schedule(
                requestDto.getUsername(),
                requestDto.getTitle(),
                requestDto.getContent());
        scheduleRepository.save(schedule);
    }

    public Page<ScheduleCommentDto> getAllSchedules(int Page) {
        Pageable pageable = PageRequest.of(Page, 10); // 페이지 크기 10
        return scheduleRepository.findAllByOrderByModifiedAtDesc(pageable)
                .map(schedule -> new ScheduleCommentDto(
                        schedule.getTitle(),
                        schedule.getContent(),
                        (long) schedule.getComments().size(), // 댓글 개수
                        schedule.getCreatedAt(),
                        schedule.getModifiedAt(),
                        schedule.getUsername()
                ));
    }

    public Optional<ScheduleResponseDto> getScheduleById(Long id) {
        return scheduleRepository.findById(id)
                .map(ScheduleResponseDto::new);
    }

    public void updateSchedule(Long id, ScheduleRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다." + id));
        schedule.setUsername(requestDto.getUsername());
        schedule.setTitle(requestDto.getTitle());
        schedule.setContent(requestDto.getContent());
        scheduleRepository.save(schedule);
    }

    public void deleteScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일정을 찾을 수 없습니다: " + id));

        commentRepository.deleteAll(schedule.getComments()); // 댓글을 먼저 삭제
        scheduleRepository.delete(schedule); // 댓글 삭제 후 일정 삭제
    }
}
