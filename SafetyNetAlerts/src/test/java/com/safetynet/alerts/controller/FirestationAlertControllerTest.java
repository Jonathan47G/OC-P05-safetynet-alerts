package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FirestationPersonDto;
import com.safetynet.alerts.dto.FirestationResponseDto;
import com.safetynet.alerts.service.FirestationAlertService;
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

@WebMvcTest(FirestationAlertController.class)
class FirestationAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FirestationAlertService firestationAlertService;

    @Test
    void getFirestationShouldReturnPersonsAndCounts()
            throws Exception {

        FirestationResponseDto response =
                new FirestationResponseDto(
                        List.of(
                                new FirestationPersonDto(
                                        "John",
                                        "Boyd",
                                        "1509 Culver St",
                                        "111-111-1111"
                                )
                        ),
                        1,
                        0
                );

        when(
                firestationAlertService
                        .getPersonsByStation("3")
        ).thenReturn(response);

        mockMvc.perform(
                        get("/firestation")
                                .param(
                                        "stationNumber",
                                        "3"
                                )
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "persons": [
                            {
                              "firstName": "John",
                              "lastName": "Boyd",
                              "address": "1509 Culver St",
                              "phone": "111-111-1111"
                            }
                          ],
                          "adultCount": 1,
                          "childCount": 0
                        }
                        """));
    }

    @Test
    void getFirestationShouldReturnEmptyResponseForUnknownStation()
            throws Exception {

        FirestationResponseDto response =
                new FirestationResponseDto(
                        List.of(),
                        0,
                        0
                );

        when(
                firestationAlertService
                        .getPersonsByStation("999")
        ).thenReturn(response);

        mockMvc.perform(
                        get("/firestation")
                                .param(
                                        "stationNumber",
                                        "999"
                                )
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                          "persons": [],
                          "adultCount": 0,
                          "childCount": 0
                        }
                        """));
    }

    @Test
    void getFirestationShouldReturnBadRequestWithoutStationNumber()
            throws Exception {

        mockMvc.perform(
                        get("/firestation")
                )
                .andExpect(status().isBadRequest());
    }
}