package com.todos.api.service;

import com.todos.api.model.Todo;
import com.todos.api.repo.TodoRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
public class TodoService {

    @Autowired
    private TodoRepo todoRepo;

    public Todo addTodo(Todo todo) throws RuntimeException {
        todo.setPersonId((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        todo.setId(new ObjectId().toString());
        return todoRepo.addTodo(todo)
                .orElseThrow( ()->new RuntimeException("Person with id " + todo.getPersonId() + " was not found!"));
    }

    public Todo updateTodo(Todo todo) throws RuntimeException {
        todo.setPersonId((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return todoRepo.updateTodo(todo)
                .orElseThrow( ()->new RuntimeException("Todo with id " + todo.getId() + " was not found!"));
    }

    public void deleteTodo(String todoId){
        String personId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        todoRepo.deleteTodo(todoId, personId);
    }
}
