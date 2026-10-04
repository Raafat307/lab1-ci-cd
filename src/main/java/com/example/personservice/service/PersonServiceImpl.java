package com.example.personservice.service;

import com.example.personservice.domain.Person;
import com.example.personservice.dto.PersonRequest;
import com.example.personservice.dto.PersonResponse;
import com.example.personservice.exception.PersonNotFoundException;
import com.example.personservice.repository.PersonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PersonResponse> getAllPersons() {
        return personRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PersonResponse getPersonById(Integer id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
        return toResponse(person);
    }

    @Override
    public Integer createPerson(PersonRequest request) {
        Person person = new Person(
                request.name(),
                request.age(),
                request.address(),
                request.work()
        );
        Person saved = personRepository.save(person);
        return saved.getId();
    }

    @Override
    public PersonResponse updatePerson(Integer id, PersonRequest request) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));

        if (request.name() != null) {
            person.setName(request.name());
        }
        if (request.age() != null) {
            person.setAge(request.age());
        }
        if (request.address() != null) {
            person.setAddress(request.address());
        }
        if (request.work() != null) {
            person.setWork(request.work());
        }

        Person updated = personRepository.save(person);
        return toResponse(updated);
    }

    @Override
    public void deletePerson(Integer id) {
        if (!personRepository.existsById(id)) {
            throw new PersonNotFoundException(id);
        }
        personRepository.deleteById(id);
    }

    private PersonResponse toResponse(Person person) {
        return new PersonResponse(
                person.getId(),
                person.getName(),
                person.getAge(),
                person.getAddress(),
                person.getWork()
        );
    }
}