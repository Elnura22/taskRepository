package com.example.testTaskMega.service.impl;

import com.example.testTaskMega.bean.CreateWorkerRequest;
import com.example.testTaskMega.controller.WorkerController;
import com.example.testTaskMega.model.Worker;
import com.example.testTaskMega.repository.WorkerRepository;
import com.example.testTaskMega.service.WorkerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WorkerServiceImpl implements WorkerService {

    @Autowired
    private WorkerRepository workerRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerController.class);

    @Override
    public Worker getWorkerById(UUID id) {
        LOGGER.info("getWorkerById: {}", id);
        Worker worker = null;
        try {
            worker = workerRepository.findById(id).orElseThrow();
            LOGGER.info("Worker found: {}", worker);
            return worker;
        }catch (Exception e){
            LOGGER.error("Worker not found: {}", id);
            e.printStackTrace();
        }
        return worker;
    }

    @Override
    public ResponseEntity<?> createWorker(CreateWorkerRequest request) {
        LOGGER.info("createWorker: {}", request);
        Worker worker = Worker.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
        workerRepository.save(worker);
        LOGGER.info("Worker created: {}", worker);
        return ResponseEntity.ok("Worker created successfully");
    }

}
