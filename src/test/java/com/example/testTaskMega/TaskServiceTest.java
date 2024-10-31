package com.example.testTaskMega;

import com.example.testTaskMega.bean.InfoTaskRequest;
import com.example.testTaskMega.model.Priority;
import com.example.testTaskMega.model.Task;
import com.example.testTaskMega.model.Worker;
import com.example.testTaskMega.repository.TaskRepository;
import com.example.testTaskMega.repository.WorkerRepository;
import com.example.testTaskMega.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private WorkerRepository workerRepository;
    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskServiceImpl taskService;

    private InfoTaskRequest request;

    private final Worker assignee = new Worker();
    private final Worker creator = new Worker();

    @BeforeEach
    public void setUp() {
        request = new InfoTaskRequest();
        request.setTitle("New Task");
        request.setDescription("Task Description");
        request.setAssigneeId(UUID.fromString("56803301-b283-4c8c-a1ef-38a2ac701b2a".trim()));
        request.setCreatorId(UUID.fromString("d3f0baf4-40cf-4110-bc1e-4083a2e95a86".trim()));
        request.setPriority(Priority.HIGH);

        assignee.setId(request.getAssigneeId());
        creator.setId(request.getCreatorId());

    }

    @Test
    void testCreateTask() {
        when(workerRepository.findById(request.getAssigneeId())).thenReturn(Optional.of(assignee));

        when(workerRepository.findById(request.getCreatorId())).thenReturn(Optional.of(creator));

        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setAssignee(assignee);
        task.setCreator(creator);
        task.setPriority(request.getPriority());
        task.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        task.setUpdatedAt(null);

        when(taskRepository.save(any(Task.class))).thenReturn(task);
        ResponseEntity<?> response = taskService.createTask(request);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(workerRepository, times(1)).findById(request.getAssigneeId());
        verify(workerRepository, times(1)).findById(request.getCreatorId());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    //done
    @Test
    void testGetTaskById() {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setAssignee(assignee);
        task.setCreator(creator);
        task.setPriority(request.getPriority());
        task.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        task.setUpdatedAt(null);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        Task task1 = taskService.getTaskById(task.getId());
        assertNotNull(task1);

        assertEquals(task.getId(), task1.getId());
        assertEquals(task.getTitle(), task1.getTitle());
        assertEquals(task.getDescription(), task1.getDescription());
        assertEquals(task.getAssignee(), task1.getAssignee());
        assertEquals(task.getCreator(), task1.getCreator());
        assertEquals(task.getPriority(), task1.getPriority());
        verify(taskRepository, times(1)).findById(task.getId());
    }

    @Test
    void testUpdateTask() {
        when(workerRepository.findById(request.getAssigneeId())).thenReturn(Optional.of(assignee));

        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setAssignee(assignee);
        task.setCreator(creator);
        task.setPriority(request.getPriority());
        task.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        when(taskRepository.findById(any(UUID.class))).thenReturn(java.util.Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        ResponseEntity<?> response = taskService.updateTask(task.getId(), request);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void deleteTask() {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle("Test Task");
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        taskService.deleteTask(task.getId());
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void testGetAllTasks() {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle(request.getTitle());
        when(taskRepository.findAll()).thenReturn(java.util.List.of(task));
        taskService.allTasks();
        verify(taskRepository, times(1)).findAll();
    }
}
