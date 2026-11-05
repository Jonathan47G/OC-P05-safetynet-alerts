package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FirePersonDto;
import com.safetynet.alerts.dto.FireResponseDto;
import com.safetynet.alerts.service.FireService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FireController.class)
class FireControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FireService fireService;

    @Test
    void getFireShouldReturnFireInformation()
            throws Exception {

        FireResponseDto response =
                new FireResponseDto(
                        "3",
                        List.of(
                                new FirePersonDto(
                                        "John",
                                        "Boyd",
                                        "841-874-6512",
                                        42,
                                        List.of("aznol:350mg"),
                                        List.of("nillacilan")
                                )
                        )
                );

        when(
                fireService.getPersonsByAddress(
                        "1509 Culver St"
                )
        ).thenReturn(Optional.of(response));

        mockMvc.perform(
                        get("/fire")
                                .param(
                                        "address",
                                        "1509 Culver St"
                                )
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "stationNumber": "3",
                          "persons": [
                            {
                              "firstName": "John",
                              "lastName": "Boyd",
                              "phone": "841-874-6512",
                              "age": 42,
                              "medications": [
                                "aznol:350mg"
                              ],
                              "allergies": [
                                "nillacilan"
                              ]
                            }
                          ]
                        }
                        """));
    }

    @Test
    void getFireShouldReturnEmptyObjectForUnknownAddress()
            throws Exception {

        when(
                fireService.getPersonsByAddress(
                        "Unknown"
                )
        ).thenReturn(Optional.empty());

        mockMvc.perform(
                        get("/fire")
                                .param(
                                        "address",
                                        "Unknown"
                                )
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }

    @Test
    void getFireShouldReturnBadRequestWithoutAddress()
            throws Exception {

        mockMvc.perform(
                        get("/fire")
                )
                .andExpect(status().isBadRequest());
    }
}