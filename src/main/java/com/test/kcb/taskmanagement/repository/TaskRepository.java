package com.test.kcb.taskmanagement.repository;

import com.test.kcb.taskmanagement.entity.Task;
import com.test.kcb.taskmanagement.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByProjectIdAndStatusAndDueDate(
            Long projectId,
            Status status,
            LocalDate dueDate,
            Pageable pageable);

    Page<Task> findByProjectId(Long projectId, Pageable pageable);
    List<Task> findByProject_Id(Long projectId);
    Page<Task> findByProject_IdAndStatus(Long projectId, Status status, Pageable pageable);
    Page<Task> findByProject_IdAndDueDate(Long projectId, LocalDate dueDate, Pageable pageable);
    List<Task> findByProject_IdAndStatusAndDueDate(Long projectId, Status status, LocalDate dueDate);

}


