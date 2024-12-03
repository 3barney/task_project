package com.test.kcb.taskmanagement.dto.response;

public record ProjectResponse(
        Long projectId,
        String name,
        String description
) { }
