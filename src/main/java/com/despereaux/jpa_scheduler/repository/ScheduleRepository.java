package com.despereaux.jpa_scheduler.repository;

import com.despereaux.jpa_scheduler.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Page<Schedule> findAllByOrderByModifiedAtDesc(Pageable pageable); // 수정일 기준 내림차순
}
