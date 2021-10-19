package com.todos.api.controller;

import com.todos.api.model.Person;
import com.todos.api.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class PersonController {
    private final PersonService personService;

    @PostMapping("signup")
    public ResponseEntity<Person> signUp(@RequestBody Person person){
        HttpStatus httpStatus;
        try{
            personService.save(person);
            httpStatus = HttpStatus.CREATED;
        }catch (DuplicateKeyException e){
            e.printStackTrace();
            httpStatus = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(null, httpStatus);
    }

}
