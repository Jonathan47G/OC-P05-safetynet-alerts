package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FloodHouseholdDto;
import com.safetynet.alerts.dto.FloodPersonDto;
import com.safetynet.alerts.service.FloodService;
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

@WebMvcTest(FloodController.class)
class FloodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FloodService floodService;

    @Test
    void getFloodStationsShouldReturnHouseholds()
            throws Exception {

        FloodPersonDto person =
                new FloodPersonDto(
                        "John",
                        "Boyd",
                        "841-874-6512",
                        42,
                        List.of("aznol:350mg"),
                        List.of("nillacilan")
                );

        FloodHouseholdDto household =
                new FloodHouseholdDto(
                        "1509 Culver St",
                        List.of(person)
                );

        when(
                floodService.getHouseholdsByStations(
                        List.of("2", "3")
                )
        ).thenReturn(List.of(household));

        mockMvc.perform(
                        get("/flood/stations")
                                .param("stations", "2,3")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                          {
                            "address": "1509 Culver St",
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
                        ]
                        """));
    }

    @Test
    void getFloodStationsShouldReturnBadRequestWithoutStations()
            throws Exception {

        mockMvc.perform(
                        get("/flood/stations")
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFloodStationsShouldReturnEmptyObjectForUnknownStation()
            throws Exception {

        when(
                floodService.getHouseholdsByStations(
                        List.of("999")
                )
        ).thenReturn(List.of());

        mockMvc.perform(
                        get("/flood/stations")
                                .param("stations", "999")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }

}