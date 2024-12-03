package com.test.kcb.taskmanagement.controller;

import com.test.kcb.taskmanagement.dto.request.CreateTask;
import com.test.kcb.taskmanagement.dto.response.TaskResponse;
import com.test.kcb.taskmanagement.enums.Status;
import com.test.kcb.taskmanagement.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Tasks", description = "Operations related to tasks")
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "Add a task to a project", description = "Adds a new task to the specified project.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<TaskResponse> addTask(
            @PathVariable Long projectId, @Valid @RequestBody CreateTask task) {
        TaskResponse createdTask = taskService.addTaskToProject(projectId, task);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @Operation(summary = "Get tasks for a project", description = "Retrieves tasks for the specified project, with optional filtering and pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = TaskResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Project not found",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTasks(
            @Parameter(description = "ID of the project", required = true)
            @PathVariable Long projectId,
            @Parameter(description = "Filter tasks by status")
            @RequestParam(required = false) Status status,
            @Parameter(description = "Filter tasks by due date")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dueDate,
            @Parameter(hidden = true) Pageable pageable) {

        List<TaskResponse> tasks = taskService.getTasksByProject(projectId, status, dueDate, pageable);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @Operation(summary = "Update a task", description = "Updates the details of an existing task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task updated successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = TaskResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content)
    })
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long taskId, @Valid @RequestBody CreateTask taskDetails) {
        TaskResponse updatedTask = taskService.updateTask(taskId, taskDetails);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }

    @Operation(summary = "Delete a task", description = "Deletes an existing task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task deleted successfully",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content)
    })
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}

