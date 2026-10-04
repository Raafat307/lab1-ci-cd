package com.example.personservice.web;

import com.example.personservice.PersonServiceApplication;
import com.example.personservice.dto.PersonRequest;
import com.example.personservice.repository.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = PersonServiceApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {
        personRepository.deleteAll();
    }

    // ============ Test 1: POST creates person with 201 + Location ============
    @Test
    void createPerson_shouldReturn201WithLocation() throws Exception {
        PersonRequest request = new PersonRequest("Ali Ahmed", 31, "Moscow", "BMSTU");

        mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", containsString("/api/v1/persons/")));
    }

    // ============ Test 2: GET by id returns person ============
    @Test
    void getPersonById_shouldReturnPerson() throws Exception {
        // Create via POST to get a real id
        PersonRequest request = new PersonRequest("Sara Ivanova", 25, "Saint Petersburg", "Google");
        String location = mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn()
                .getResponse()
                .getHeader("Location");

        String id = location.substring(location.lastIndexOf("/") + 1);

        mockMvc.perform(get("/api/v1/persons/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(Integer.parseInt(id)))
                .andExpect(jsonPath("$.name").value("Sara Ivanova"))
                .andExpect(jsonPath("$.age").value(25))
                .andExpect(jsonPath("$.address").value("Saint Petersburg"))
                .andExpect(jsonPath("$.work").value("Google"));
    }

    // ============ Test 3: GET by id returns 404 when not found ============
    @Test
    void getPersonById_shouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/v1/persons/{id}", 99999))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").exists());
    }

    // ============ Test 4: GET all returns array ============
    @Test
    void getAllPersons_shouldReturnList() throws Exception {
        // Create two persons
        mockMvc.perform(post("/api/v1/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new PersonRequest("Person One", 20, "City A", "Company A"))));

        mockMvc.perform(post("/api/v1/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new PersonRequest("Person Two", 30, "City B", "Company B"))));

        mockMvc.perform(get("/api/v1/persons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder("Person One", "Person Two")));
    }

    // ============ Test 5: PATCH updates and DELETE removes ============
    @Test
    void updateAndDeletePerson_shouldWork() throws Exception {
        // Create
        PersonRequest createReq = new PersonRequest("Original", 40, "Old City", "Old Job");
        String location = mockMvc.perform(post("/api/v1/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andReturn().getResponse().getHeader("Location");

        String id = location.substring(location.lastIndexOf("/") + 1);

        // PATCH: update name only
        PersonRequest updateReq = new PersonRequest("Updated", null, null, null);
        mockMvc.perform(patch("/api/v1/persons/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"))
                .andExpect(jsonPath("$.age").value(40))
                .andExpect(jsonPath("$.address").value("Old City"))
                .andExpect(jsonPath("$.work").value("Old Job"));

        // DELETE
        mockMvc.perform(delete("/api/v1/persons/{id}", id))
                .andExpect(status().isNoContent());

        // Verify deleted
        mockMvc.perform(get("/api/v1/persons/{id}", id))
                .andExpect(status().isNotFound());
    }
}