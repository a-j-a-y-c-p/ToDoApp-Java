package com.example.todoapp_java.repository;

import com.example.todoapp_java.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
