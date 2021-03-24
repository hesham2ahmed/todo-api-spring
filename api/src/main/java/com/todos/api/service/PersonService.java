package com.todos.api.service;

import com.todos.api.model.Person;
import com.todos.api.model.Todo;
import com.todos.api.repo.PersonRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService implements UserDetailsService {
    @Autowired
    private PersonRepo personRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Person> personOptional = personRepo.findPersonByEmail(email);
        return personOptional.orElseThrow(()-> new UsernameNotFoundException("User with email " + email + " not found"));
    }

    public void save(Person person) throws DuplicateKeyException{
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personRepo.save(person)
                .orElseThrow(()->new DuplicateKeyException("Person with email " + person.getEmail() + " is already exist!"));
        return;
    }

}
