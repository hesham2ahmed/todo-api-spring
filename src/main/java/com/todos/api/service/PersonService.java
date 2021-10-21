package com.todos.api.service;

import com.todos.api.model.Person;
import com.todos.api.repo.PersonRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PersonService implements UserDetailsService {
    private final PersonRepo personRepo;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Person> personOptional = personRepo.findPersonByEmail(email.toLowerCase());
        return personOptional.orElseThrow(()-> new UsernameNotFoundException("User with email " + email + " not found"));
    }

    public void save(Person person) throws DuplicateKeyException{
        person.setEmail(person.getEmail().toLowerCase());
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setJoinDate(new Date());
        person.setTodos(new ArrayList<>());
        personRepo.save(person)
                .orElseThrow(()->new DuplicateKeyException("Person with email " + person.getEmail() + " is already exist!"));
    }

}
