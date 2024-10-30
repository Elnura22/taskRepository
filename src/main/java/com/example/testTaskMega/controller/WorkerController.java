package com.example.testTaskMega.controller;

import com.example.testTaskMega.bean.CreateWorkerRequest;
import com.example.testTaskMega.bean.StatusResponse;
import com.example.testTaskMega.model.Worker;
import com.example.testTaskMega.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/workers")
public class WorkerController {
    @Autowired
    private WorkerService workerService;

    @PostMapping("/createWorker")
    public ResponseEntity<?> createWorker(@RequestBody CreateWorkerRequest request) {
        return workerService.createWorker(request);
    }

    @GetMapping("/getWorkerById")
    public Worker getWorkerById(@RequestParam UUID id) {
        return workerService.getWorkerById(id);
    }
}
