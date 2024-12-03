package com.test.kcb.taskmanagement.controller;

import com.test.kcb.taskmanagement.dto.request.CreateTask;
import com.test.kcb.taskmanagement.dto.response.TaskResponse;
import com.test.kcb.taskmanagement.enums.Status;
import com.test.kcb.taskmanagement.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/projects/{projectId}/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> addTask(
            @PathVariable Long projectId, @Valid @RequestBody CreateTask task) {
        TaskResponse createdTask = taskService.addTaskToProject(projectId, task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(
            @PathVariable Long projectId,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
            Pageable pageable) {

        List<TaskResponse> tasks = taskService.getTasksByProject(projectId, status, dueDate, pageable);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long taskId, @Valid @RequestBody CreateTask taskDetails) {
        TaskResponse updatedTask = taskService.updateTask(taskId, taskDetails);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}

