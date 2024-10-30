package com.example.testTaskMega;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = "com.example.testTaskMega.repository")
@SpringBootApplication
public class TestTaskMegaApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestTaskMegaApplication.class, args);
    }

}
