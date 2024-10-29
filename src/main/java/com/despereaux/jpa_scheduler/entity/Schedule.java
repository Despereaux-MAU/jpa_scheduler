package com.despereaux.jpa_scheduler.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "schedule")
public class Schedule extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String title;
    private String content;

    public Schedule(String username, String title, String content) {
        this.username = username;
        this.title = title;
        this.content = content;
    }
}
