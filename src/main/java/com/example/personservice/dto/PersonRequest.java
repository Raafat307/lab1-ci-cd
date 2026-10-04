package com.example.personservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PersonRequest(

        @NotBlank(message = "Name must not be blank")
        @Size(max = 80, message = "Name must not exceed 80 characters")
        String name,

        @Positive(message = "Age must be positive")
        Integer age,

        @Size(max = 255, message = "Address must not exceed 255 characters")
        String address,

        @Size(max = 255, message = "Work must not exceed 255 characters")
        String work
) {
}