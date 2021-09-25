package com.todos.api.repo;
import com.todos.api.model.Person;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class PersonRepo{
    private final MongoTemplate mongoTemplate;

    public Optional<Person> save(Person person)  {
        Optional<Person> optionalPerson;
        try{
            person = mongoTemplate.save(person);
            optionalPerson = Optional.of(person);
        }catch (DuplicateKeyException e){
            optionalPerson = Optional.empty();
        }
        return optionalPerson;
    }

    public Optional<Person> findPersonByEmail(String email){
        return Optional.ofNullable(this.mongoTemplate.findOne(Query.query(Criteria.where("email").is(email)),Person.class));
    }

    public Optional<Person> findPersonByID(String id){
        return Optional.ofNullable(this.mongoTemplate.findOne(Query.query(Criteria.where("_id").is(id)),Person.class));
    }

    public List<Person> findAll(){
       return this.mongoTemplate.findAll(Person.class);
    }

    public void deleteAll(){
        this.mongoTemplate.dropCollection(Person.class);
    }

}
