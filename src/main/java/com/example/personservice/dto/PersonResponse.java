package com.example.personservice.dto;

public record PersonResponse(
        Integer id,
        String name,
        Integer age,
        String address,
        String work
) {
}