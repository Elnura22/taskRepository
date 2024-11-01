package com.example.testTaskMega;

import com.example.testTaskMega.bean.CreateWorkerRequest;
import com.example.testTaskMega.model.Worker;
import com.example.testTaskMega.repository.WorkerRepository;
import com.example.testTaskMega.service.impl.WorkerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkerServiceTest {
    @Mock
    private WorkerRepository workerRepository;

    @InjectMocks
    private WorkerServiceImpl workerService;

    @Value("${email.password")
    private String password;
    private CreateWorkerRequest request;
    private Worker worker;

    @BeforeEach
    public void setUp() {
        request = new CreateWorkerRequest();
        request.setEmail("wlyaeth.mn@gmail.com");
        request.setPassword("password");
        request.setName("elnura");

        worker = new Worker();
        worker.setId(UUID.randomUUID());
        worker.setEmail(request.getEmail());
        worker.setPassword(request.getPassword());
        worker.setName(request.getName());
    }

    @Test
    void testCreateWorker() {
        when(workerRepository.save(any(Worker.class))).thenReturn(worker);
        ResponseEntity<?> response = workerService.createWorker(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(workerRepository, times(1)).save(any(Worker.class));
    }

}
