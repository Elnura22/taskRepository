package com.example.testTaskMega.repository;

import com.example.testTaskMega.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface WorkerRepository extends JpaRepository<Worker, UUID> {

    Optional<Worker> findById(UUID id);


}
