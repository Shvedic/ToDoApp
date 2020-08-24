package com.example.todo.repos;

import com.example.todo.domain.Task;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TaskRepo extends CrudRepository<Task, Long> {

    List<Task> findTaskByTextContains(String text);
}
