package com.example.testTaskMega.service;

import com.example.testTaskMega.bean.InfoTaskRequest;
import com.example.testTaskMega.bean.StatusResponse;
import com.example.testTaskMega.model.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface TaskService {
    Task getTaskById(UUID id);

    ResponseEntity<?> createTask(InfoTaskRequest request);

    ResponseEntity<StatusResponse> updateTask(UUID id, InfoTaskRequest request);

    ResponseEntity<StatusResponse> deleteTask(UUID id);

    List<Task> allTasks();
}
