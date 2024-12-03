package com.test.kcb.taskmanagement.service.impl;

import com.test.kcb.taskmanagement.dto.request.CreateTask;
import com.test.kcb.taskmanagement.dto.response.TaskResponse;
import com.test.kcb.taskmanagement.entity.Project;
import com.test.kcb.taskmanagement.entity.Task;
import com.test.kcb.taskmanagement.enums.Status;
import com.test.kcb.taskmanagement.exception.ProjectNotFoundException;
import com.test.kcb.taskmanagement.exception.TaskNotFoundException;
import com.test.kcb.taskmanagement.repository.ProjectRepository;
import com.test.kcb.taskmanagement.repository.TaskRepository;
import com.test.kcb.taskmanagement.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;


    @Override
    public TaskResponse addTaskToProject(Long projectId, CreateTask task) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found with ID: " + projectId));

        Task taskEntity = createTaskEntity(task, project);

        Task savedTask = taskRepository.save(taskEntity);
        return createTaskResponseFromEntity(savedTask);
    }

    @Override
    public List<TaskResponse> getTasksByProject(Long projectId, Status status, LocalDate dueDate, Pageable pageable) {
        Page<Task> tasks;

        if (status != null && dueDate != null) {
            tasks = taskRepository.findByProject_IdAndStatusAndDueDate(projectId, status, dueDate, pageable);
        } else if (status != null) {
            tasks = taskRepository.findByProject_IdAndStatus(projectId, status, pageable);
        } else if (dueDate != null) {
            tasks = taskRepository.findByProject_IdAndDueDate(projectId, dueDate, pageable);
        } else {
            tasks = taskRepository.findByProjectId(projectId, pageable);
        }

        return tasks.stream()
                .map(this::createTaskResponseFromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public TaskResponse updateTask(Long taskId, CreateTask task) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + taskId));

        Task updatedTask = updateExistingTask(existingTask, task);
        Task savedTask = taskRepository.save(updatedTask);
        return createTaskResponseFromEntity(savedTask);
    }

    @Override
    public Void deleteTask(Long taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
        } else {
            throw new TaskNotFoundException("Task not found with ID: " + taskId);
        }
        return null;
    }

    private Task createTaskEntity(CreateTask task, Project project) {
        Task taskEntity = new Task();
        taskEntity.setTitle(task.title());
        taskEntity.setDescription(task.description());
        taskEntity.setStatus(task.status());
        taskEntity.setDueDate(task.dueDate());
        taskEntity.setProject(project);
        return taskEntity;
    }

    private TaskResponse createTaskResponseFromEntity(Task entity) {
        Long projectId = entity.getProject() != null ? entity.getProject().getId() : null;

        return new TaskResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getDueDate(),
                projectId
        );
    }

    private Task updateExistingTask(Task existingTask, CreateTask task) {
        if (task.dueDate() != null) {
            existingTask.setDueDate(task.dueDate());
        }
        if (task.status() != null) {
            existingTask.setStatus(task.status());
        }
        if (task.description() != null && !task.description().isEmpty()) {
            existingTask.setDescription(task.description());
        }
        if (task.title() != null && !task.title().isEmpty()) {
            existingTask.setTitle(task.title());
        }
        return existingTask;
    }
}
