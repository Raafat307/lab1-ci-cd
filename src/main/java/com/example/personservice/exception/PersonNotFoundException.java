package com.example.personservice.exception;

public class PersonNotFoundException extends RuntimeException {

    public PersonNotFoundException(Integer id) {
        super("Person with id " + id + " not found");
    }
}