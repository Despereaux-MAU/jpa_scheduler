package com.despereaux.jpa_scheduler.repository;

import com.despereaux.jpa_scheduler.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
