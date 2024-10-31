package com.despereaux.jpa_scheduler.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "schedule")
public class Schedule extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private String weather;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToMany(mappedBy = "assignedSchedules")
    private List<User> assignedUsers = new ArrayList<>();

    public static Schedule create(String title, String content, String weather, User user) {
        Schedule schedule = new Schedule();
        schedule.title = title;
        schedule.content = content;
        schedule.weather = weather;
        schedule.user = user;
        return schedule;
    }

    public void update(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }
}
