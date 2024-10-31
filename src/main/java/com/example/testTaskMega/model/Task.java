package com.example.testTaskMega.model;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;
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
    private Timestamp createdAt;
    @Nullable
    private Timestamp updatedAt;
    @jakarta.persistence.ManyToOne
    @jakarta.persistence.JoinColumn(name = "assignee_id")
    private Worker assignee;
    @jakarta.persistence.ManyToOne
    @jakarta.persistence.JoinColumn(name = "creator_id")
    private Worker creator;
    @Enumerated(EnumType.STRING)
    private Priority priority;

}
