package com.example.personservice.service;

import com.example.personservice.dto.PersonRequest;
import com.example.personservice.dto.PersonResponse;

import java.util.List;

public interface PersonService {

    List<PersonResponse> getAllPersons();

    PersonResponse getPersonById(Integer id);

    Integer createPerson(PersonRequest request);

    PersonResponse updatePerson(Integer id, PersonRequest request);

    void deletePerson(Integer id);
}