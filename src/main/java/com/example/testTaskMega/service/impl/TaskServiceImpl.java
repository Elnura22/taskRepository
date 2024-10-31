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
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CacheConfig(cacheNames = "tasksCache")
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

    @CacheEvict(cacheNames = "tasksCache", allEntries = true)
    @Override
    public ResponseEntity<?> createTask(InfoTaskRequest request) {
        LOGGER.info("createTask: {}", request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        try {
            Optional<Worker> assignee = workerRepository.findById(request.getAssigneeId());
            if (assignee.isEmpty()) {
                return new ResponseEntity<>(StatusResponse.builder().code(404).message("Assignee not found, try again").build(), HttpStatus.OK);
            }
            Optional<Worker> creator = workerRepository.findById(request.getCreatorId());
            if (creator.isEmpty()) {
                return new ResponseEntity<>(StatusResponse.builder().code(404).message("Creator not found, try again").build(), HttpStatus.OK);
            }
            Task task = Task.builder()
                    .id(UUID.randomUUID())
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .createdAt(Timestamp.valueOf(LocalDateTime.now()))
                    .updatedAt(null)
                    .assignee(assignee.get())
                    .creator(creator.get())
                    .priority(request.getPriority())
                    .build();
            taskRepository.save(task);
            mailService.sendEmail(assignee.get().getEmail(), "You have new task", "Task: " + task);
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
        Optional<Task> taskOptional = taskRepository.findById(id);
        Optional<Worker> assignee = workerRepository.findById(request.getAssigneeId());
        if (assignee.isEmpty()) {
            return new ResponseEntity<>(StatusResponse.builder().code(404).message("Assignee not found, try again").build(), HttpStatus.OK);
        }
        if (taskOptional.isEmpty()) {
            return new ResponseEntity<>(StatusResponse.builder().code(404).message("Task not found, try again").build(), HttpStatus.OK);
        }
        Task task = taskOptional.get();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setAssignee(assignee.get());
        task.setPriority(request.getPriority());
        task.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        taskRepository.save(task);
        LOGGER.info("Task updated: {}", task);
        return new ResponseEntity<>(StatusResponse.builder().code(200).message("Task updated successfully").build(), HttpStatus.OK);
    }


    @CacheEvict(cacheNames = "tasksCache", allEntries = true)
    @Override
    public ResponseEntity<StatusResponse> deleteTask(UUID id) {
        LOGGER.info("deleteTask: {}", id);
        Optional<Task> taskOptional = taskRepository.findById(id);
        if (taskOptional.isEmpty()) {
            return new ResponseEntity<>(StatusResponse.builder().code(404).message("Task not found, try again").build(), HttpStatus.OK);
        }
        Task task = taskOptional.get();
        taskRepository.delete(task);
        LOGGER.info("Task deleted: {}", task);
        return new ResponseEntity<>(StatusResponse.builder().code(200).message("Task deleted successfuly").build(), HttpStatus.OK);

    }


    @Cacheable(cacheNames = "tasksСache")
    @Override
    public List<Task> allTasks() {
        waitSomeTime();
        List<Task> taskList = taskRepository.findAll();
        LOGGER.info("allTasks: {}", taskList);
        return taskList;
    }

    private void waitSomeTime() {
        System.out.println("Long Wait Begin");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Long Wait End");
    }

    @Override
    public ResponseEntity<?> sendApiRequest() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(API_URL, String.class);
        LOGGER.info("API response: {}", response.getBody());
        return new ResponseEntity<>(StatusResponse.builder().code(200).message(response.getBody()).build(), HttpStatus.OK);
    }
}
