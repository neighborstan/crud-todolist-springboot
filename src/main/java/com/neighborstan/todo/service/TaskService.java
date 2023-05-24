package com.neighborstan.todo.service;


import com.neighborstan.todo.domain.Task;
import com.neighborstan.todo.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    public Page<Task> showTasks(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void saveTask(Task task) {
        repository.save(task);
    }

    public void deleteTask(Integer id) {
        repository.deleteById(id);
    }

    public Optional<Task> findTaskById(Integer id) {
        return repository.findById(id);
    }
}
