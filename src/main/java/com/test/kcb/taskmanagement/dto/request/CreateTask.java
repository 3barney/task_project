package com.test.kcb.taskmanagement.dto.request;

import com.test.kcb.taskmanagement.enums.Status;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateTask(
        @NotEmpty(message = "Task title cannot be empty")
        String title,

        String description,

        @NotNull(message = "Status cannot be null")
        Status status,

        @FutureOrPresent(message = "Due date must be in the present or future")
        LocalDate dueDate,

        @NotNull(message = "Project ID cannot be null")
        Long projectId
) { }