package com.todos.api.repo;

import com.todos.api.model.Person;
import com.todos.api.model.Todo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TodoRepo {

    @Autowired
    private MongoTemplate mongoTemplate;

    public Optional<Todo> addTodo(Todo todo){
        Criteria criteria = Criteria.where("id").is(todo.getPersonId());
        return mongoTemplate.updateFirst(Query.query(criteria), new Update().push("todos", todo), Person.class).getMatchedCount() > 0
                ? Optional.of(todo) : Optional.empty();
    }

    public Optional<Todo> updateTodo(Todo todo){
        return deleteTodo(todo.getId(), todo.getPersonId()) ? addTodo(todo) : Optional.empty();
    }

    public boolean deleteTodo(String todoId, String personId)
    {
        Criteria criteria = Criteria.where("id").is(personId);
        return mongoTemplate.updateFirst(Query.query(criteria), new Update().pull("todos", Todo.builder().id(todoId).build()), Person.class).getModifiedCount() > 0;
    }

}
