package com.example.testTaskMega.bean;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateWorkerRequest {
    @NonNull
    private String name;
    private String email;
    private String password;
}
