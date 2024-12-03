package com.test.kcb.taskmanagement.dto.response;

import com.test.kcb.taskmanagement.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectSummaryResponse {
    private Long projectId;
    private String projectName;
    private Map<Status, Long> taskCountByStatus;
}
