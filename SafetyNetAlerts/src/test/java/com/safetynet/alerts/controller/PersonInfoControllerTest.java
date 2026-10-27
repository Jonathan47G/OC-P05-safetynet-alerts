package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.PersonInfoDto;
import com.safetynet.alerts.service.PersonInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonInfoController.class)
class PersonInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonInfoService personInfoService;

    @Test
    void getPersonInfoShouldReturnPersons() throws Exception {

        PersonInfoDto person = new PersonInfoDto(
                "John",
                "Boyd",
                "1509 Culver St",
                42,
                "john@email.com",
                List.of("aznol:350mg"),
                List.of("nillacilan")
        );

        when(personInfoService.getPersonsByLastName("Boyd"))
                .thenReturn(List.of(person));

        mockMvc.perform(
                        get("/personInfo")
                                .param("lastName", "Boyd")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                          {
                            "firstName": "John",
                            "lastName": "Boyd",
                            "address": "1509 Culver St",
                            "age": 42,
                            "email": "john@email.com",
                            "medications": [
                              "aznol:350mg"
                            ],
                            "allergies": [
                              "nillacilan"
                            ]
                          }
                        ]
                        """));
    }

    @Test
    void getPersonInfoShouldReturnEmptyListForUnknownName()
            throws Exception {

        when(personInfoService.getPersonsByLastName("Unknown"))
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/personInfo")
                                .param("lastName", "Unknown")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getPersonInfoShouldReturnBadRequestWithoutLastName()
            throws Exception {

        mockMvc.perform(
                        get("/personInfo")
                )
                .andExpect(status().isBadRequest());
    }
}