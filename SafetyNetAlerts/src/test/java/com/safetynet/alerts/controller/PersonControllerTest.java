package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;

    @Test
    void addPersonShouldReturnCreatedPerson() throws Exception {

        Person person = createPerson();

        when(personService.addPerson(any(Person.class)))
                .thenReturn(person);

        mockMvc.perform(
                        post("/person")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "firstName": "John",
                                          "lastName": "Doe",
                                          "address": "1 Test St",
                                          "city": "Culver",
                                          "zip": "97451",
                                          "phone": "111-222-3333",
                                          "email": "john@test.com"
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@test.com"));
    }

    @Test
    void updatePersonShouldReturnUpdatedPerson() throws Exception {

        Person person = createPerson();
        person.setAddress("42 New Address");

        when(personService.updatePerson(any(Person.class)))
                .thenReturn(Optional.of(person));

        mockMvc.perform(
                        put("/person")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "firstName": "John",
                                          "lastName": "Doe",
                                          "address": "42 New Address",
                                          "city": "Culver",
                                          "zip": "97451",
                                          "phone": "111-222-3333",
                                          "email": "john@test.com"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address")
                        .value("42 New Address"));
    }

    @Test
    void updatePersonShouldReturnNotFoundWhenPersonDoesNotExist()
            throws Exception {

        when(personService.updatePerson(any(Person.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(
                        put("/person")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "firstName": "Unknown",
                                          "lastName": "Person",
                                          "address": "1 Test St",
                                          "city": "Culver",
                                          "zip": "97451",
                                          "phone": "111-222-3333",
                                          "email": "test@test.com"
                                        }
                                        """)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json("{}"));
    }

    @Test
    void deletePersonShouldReturnOkWhenPersonExists()
            throws Exception {

        when(personService.deletePerson("John", "Doe"))
                .thenReturn(true);

        mockMvc.perform(
                        delete("/person")
                                .param("firstName", "John")
                                .param("lastName", "Doe")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }

    @Test
    void deletePersonShouldReturnNotFoundWhenPersonDoesNotExist()
            throws Exception {

        when(personService.deletePerson("John", "Doe"))
                .thenReturn(false);

        mockMvc.perform(
                        delete("/person")
                                .param("firstName", "John")
                                .param("lastName", "Doe")
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json("{}"));
    }

    @Test
    void deletePersonShouldReturnBadRequestWithoutParameters()
            throws Exception {

        mockMvc.perform(delete("/person"))
                .andExpect(status().isBadRequest());
    }

    private Person createPerson() {

        Person person = new Person();

        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("1 Test St");
        person.setCity("Culver");
        person.setZip("97451");
        person.setPhone("111-222-3333");
        person.setEmail("john@test.com");

        return person;
    }
}