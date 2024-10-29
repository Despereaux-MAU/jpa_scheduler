package com.despereaux.jpa_scheduler.repository;

import com.despereaux.jpa_scheduler.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
