package com.todos.api.controller;

import com.todos.api.model.Todo;
import com.todos.api.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping("/add")
    public ResponseEntity<Todo> addTodo(@RequestBody Todo todo)
    {
        HttpStatus httpStatus;
        try {
            todoService.addTodo(todo);
            httpStatus = HttpStatus.CREATED;
        } catch (RuntimeException e) {
            httpStatus = HttpStatus.NOT_FOUND;
            e.printStackTrace();
        }
        return new ResponseEntity<>(todo, httpStatus);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Todo> deleteTodo(@PathVariable("id") String id){
        todoService.deleteTodo(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Todo> updateTodo(@RequestBody Todo todo){
        HttpStatus httpStatus;
        try{
            todoService.updateTodo(todo);
            httpStatus = HttpStatus.OK;
        }catch (RuntimeException e){
            httpStatus = HttpStatus.NOT_FOUND;
            e.printStackTrace();
        }
        return new ResponseEntity<>(todo, httpStatus);
    }
}
