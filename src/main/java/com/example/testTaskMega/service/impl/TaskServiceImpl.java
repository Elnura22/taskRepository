package com.example.testTaskMega.service.impl;

import com.example.testTaskMega.bean.InfoTaskRequest;
import com.example.testTaskMega.bean.StatusResponse;
import com.example.testTaskMega.controller.TaskController;
import com.example.testTaskMega.model.Task;
import com.example.testTaskMega.model.Worker;
import com.example.testTaskMega.repository.TaskRepository;
import com.example.testTaskMega.repository.WorkerRepository;
import com.example.testTaskMega.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private MailService mailService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);

    @Value("${api.url}")
    private String API_URL;

    @Override
    public Task getTaskById(UUID id) {
        return taskRepository.findById(id).orElseThrow();
    }

    @Override
    public ResponseEntity<?> createTask(InfoTaskRequest request) {
        LOGGER.info("createTask: {}", request);
        try {
            Worker assignee = workerRepository.findById(request.getAssigneeId()).orElseThrow();
            Worker creator = workerRepository.findById(request.getCreatorId()).orElseThrow();
            Task task = Task.builder()
                    .id(UUID.randomUUID())
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .createdAt(LocalDateTime.now())
                    .updatedAt(null)
                    .assignee(assignee)
                    .creator(creator)
                    .priority(request.getPriority())
                    .build();
            taskRepository.save(task);
            mailService.sendEmail("elnura.amantur@gmail.com", "You have new task", "Task: " + task);
            LOGGER.info("Task created: {}", task);
            return new ResponseEntity<>(StatusResponse.builder().code(200).message("Task created successfully").build(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(StatusResponse.builder().code(500).message("Internal server error").build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<StatusResponse> updateTask(UUID id, InfoTaskRequest request) {
        LOGGER.info("updateTask: {}", request);
        Task task = getTaskById(id);
        Worker assignee = workerRepository.findById(request.getAssigneeId()).orElse(null);
        if (task != null) {
            task.setTitle(request.getTitle());
            task.setDescription(request.getDescription());
            task.setAssignee(assignee);
            task.setPriority(request.getPriority());
            task.setUpdatedAt(LocalDateTime.now());
            taskRepository.save(task);
            LOGGER.info("Task updated: {}", task);
            return new ResponseEntity<>(StatusResponse.builder().code(200).message("Task updated successfully").build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(StatusResponse.builder().code(400).message("Task not found").build(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<StatusResponse> deleteTask(UUID id) {
        LOGGER.info("deleteTask: {}", id);
        Task task = getTaskById(id);
        if (task != null) {
            taskRepository.delete(task);
            LOGGER.info("Task deleted: {}", task);
            return new ResponseEntity<>(StatusResponse.builder().code(200).message("Task deleted successfuly").build(), HttpStatus.OK);
        }
        return new ResponseEntity<>(StatusResponse.builder().code(400).message("Task not found").build(), HttpStatus.OK);
    }

    @Override
    public List<Task> allTasks() {
        List<Task> taskList = taskRepository.findAll();
        LOGGER.info("allTasks: {}", taskList);
        return taskList;
    }

    @Override
    public ResponseEntity<?> sendApiRequest() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(API_URL, String.class);
        LOGGER.info("API response: {}", response.getBody());
        return new ResponseEntity<>(StatusResponse.builder().code(200).message(response.getBody()).build(), HttpStatus.OK);
    }
}
