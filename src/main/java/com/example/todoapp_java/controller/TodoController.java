package com.example.todoapp_java.controller;

import com.example.todoapp_java.model.Todo;
import com.example.todoapp_java.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todos")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoService.getAllTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
        Optional<Todo> todo = todoService.getTodoById(id);
        return todo.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo) {
        Todo savedTodo = todoService.saveTodo(todo);
        return new ResponseEntity<>(savedTodo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo todo) {
        if (!todoService.getTodoById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        todo.setId(id);
        Todo updatedTodo = todoService.saveTodo(todo);
        return ResponseEntity.ok(updatedTodo);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Todo> updateTodoStatus(@PathVariable Long id, @RequestBody Todo todo) {
        Optional<Todo> existingTodo = todoService.getTodoById(id);
        if (!existingTodo.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Todo updatedTodo = existingTodo.get();
        updatedTodo.setCompleted(todo.getCompleted());
        todoService.saveTodo(updatedTodo);
        return ResponseEntity.ok(updatedTodo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        if (!todoService.getTodoById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        todoService.deleteTodoById(id);
        return ResponseEntity.noContent().build();
    }
}
