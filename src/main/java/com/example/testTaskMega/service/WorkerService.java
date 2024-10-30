package com.example.testTaskMega.service;

import com.example.testTaskMega.bean.CreateWorkerRequest;
import com.example.testTaskMega.model.Worker;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface WorkerService {

    ResponseEntity<?> createWorker(CreateWorkerRequest request);

    Worker getWorkerById(UUID id);
}
