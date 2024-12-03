package com.test.kcb.taskmanagement.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record CreateProject(
        @NotEmpty(message = "Project name cannot be empty")
        String name,

        @NotEmpty(message = "Project name cannot be empty")
        String description
) {}
