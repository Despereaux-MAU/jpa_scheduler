package com.despereaux.jpa_scheduler.repository;

import com.despereaux.jpa_scheduler.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
