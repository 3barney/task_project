package com.test.kcb.taskmanagement.dto.response;

import com.test.kcb.taskmanagement.enums.Status;

import java.time.LocalDate;

public record TaskResponse(
        Long id,
        String title,
        String description,
        Status status,
        LocalDate dueDate,
        Long projectId
) { }
