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
public class Comment extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY) // 연관관계 설정 및 필요할 때 호출
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    public Comment(String commnet, String username, Schedule schedule) {
        this.username = username;
        this.comment = commnet;
        this.schedule = schedule;
    }
}
