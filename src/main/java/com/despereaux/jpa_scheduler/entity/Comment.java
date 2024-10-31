package com.despereaux.jpa_scheduler.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    public Comment(String comment, String username, Schedule schedule) {
        this.username = username;
        this.comment = comment;
        this.schedule = schedule;
    }

    public static Comment create(String commentContent, String username, Schedule schedule) {
        return new Comment(commentContent, username, schedule);
    }

    public void update(String commentContent, String username) {
        this.comment = commentContent;
        this.username = username;
    }
}
