package com.example.testTaskMega.model;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@jakarta.persistence.Entity
//@Table(name = "tasks")
public class Task {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    @Nullable
    private LocalDateTime updatedAt;
    @jakarta.persistence.ManyToOne
    @jakarta.persistence.JoinColumn(name = "assignee_id")
    private Worker assignee;
    @jakarta.persistence.ManyToOne
    @jakarta.persistence.JoinColumn(name = "creator_id")
    private Worker creator;
    @Enumerated(EnumType.STRING)
    private Priority priority;

}
