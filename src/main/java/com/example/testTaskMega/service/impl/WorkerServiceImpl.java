package com.example.testTaskMega.service.impl;

import com.example.testTaskMega.bean.CreateWorkerRequest;
import com.example.testTaskMega.model.Worker;
import com.example.testTaskMega.repository.WorkerRepository;
import com.example.testTaskMega.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class WorkerServiceImpl implements WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    @Override
    public Worker getWorkerById(UUID id) {
        Worker worker = null;
        try {
            worker = workerRepository.findById(id).orElseThrow();
            return worker;
        }catch (Exception e){
            e.printStackTrace();
        }
        return worker;
    }

    @Override
    public ResponseEntity<?> createWorker(CreateWorkerRequest request) {
        Worker worker = Worker.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .email(request.getEmail())
                .build();
        workerRepository.save(worker);
        return ResponseEntity.ok("Worker created successfully");
    }

}
