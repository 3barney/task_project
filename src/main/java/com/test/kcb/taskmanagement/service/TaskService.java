package com.test.kcb.taskmanagement.service;

import com.test.kcb.taskmanagement.dto.request.CreateTask;
import com.test.kcb.taskmanagement.dto.response.TaskResponse;
import com.test.kcb.taskmanagement.enums.Status;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    TaskResponse addTaskToProject(Long projectId, CreateTask task);

    List<TaskResponse> getTasksByProject(Long projectId, Status status, LocalDate dueDate, Pageable pageable);

    TaskResponse updateTask(Long taskId, CreateTask task);

    Void deleteTask(Long taskId);
}
