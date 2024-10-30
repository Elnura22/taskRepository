package com.example.testTaskMega.bean;

import com.example.testTaskMega.model.Priority;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfoTaskRequest {
    @NonNull
    private String title;
    @NonNull
    private String description;
    private UUID assigneeId;
    private UUID creatorId;
    private Priority priority;
}
