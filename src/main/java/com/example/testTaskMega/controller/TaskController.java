package com.example.testTaskMega.controller;

import com.example.testTaskMega.bean.InfoTaskRequest;
import com.example.testTaskMega.bean.StatusResponse;
import com.example.testTaskMega.model.Task;
import com.example.testTaskMega.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @PostMapping("/createTask")
    public ResponseEntity<?> createTask(@RequestBody InfoTaskRequest request) {
        return taskService.createTask(request);
    }

    @GetMapping("/getTaskById")
    public Task getTaskById(@RequestParam UUID id) {
        return taskService.getTaskById(id);
    }

    @PutMapping("/updateTask")
    public ResponseEntity<StatusResponse> updateTask(@RequestParam UUID id,
                                                     @RequestBody @Valid InfoTaskRequest request) {
        return taskService.updateTask(id, request);
    }

    @DeleteMapping("/deleteTask")
    public ResponseEntity<StatusResponse> deleteTask(@RequestParam UUID id) {
        return taskService.deleteTask(id);
    }

    @GetMapping("/allTasks")
    public List<Task> allTasks() {
        return taskService.allTasks();
    }

    //http api request - additional task #1
    @GetMapping("/sendApiRequest")
    public ResponseEntity<?> createApiRequest() {
        return taskService.sendApiRequest();
    }

}
