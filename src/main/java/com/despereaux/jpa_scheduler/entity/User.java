package com.despereaux.jpa_scheduler.entity;

import com.despereaux.jpa_scheduler.config.PasswordEncoder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user")
@ToString(onlyExplicitlyIncluded = true)
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "사용자명은 필수 입력 값입니다.")
    @ToString.Include
    private String username;

    @Email(message = "유효한 이메일 주소여야 합니다.")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @ToString.Include
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Schedule> schedules = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_schedule",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "schedule_id")
    )
    @ToString.Exclude
    private List<Schedule> assignedSchedules = new ArrayList<>();

    public static User createUser(String username, String email, String password, UserRoleEnum role) {
        User user = new User();
        user.username = username;
        user.email = email;
        user.password = password;
        user.role = role;
        return user;
    }

    public boolean checkPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, this.password);
    }

    public void assignRole(UserRoleEnum role) {
        this.role = role;
    }
}
