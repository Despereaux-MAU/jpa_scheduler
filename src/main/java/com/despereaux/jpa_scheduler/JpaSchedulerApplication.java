package com.despereaux.jpa_scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JpaSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaSchedulerApplication.class, args);
    }

}
