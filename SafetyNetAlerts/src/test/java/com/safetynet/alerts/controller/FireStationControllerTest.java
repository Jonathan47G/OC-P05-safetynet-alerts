package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
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

@WebMvcTest(FireStationController.class)
class FireStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FireStationService fireStationService;

    @Test
    void addFireStationShouldReturnCreatedMapping()
            throws Exception {

        FireStation fireStation =
                createFireStation("1 Test St", "5");

        when(
                fireStationService.addFireStation(
                        any(FireStation.class)
                )
        ).thenReturn(fireStation);

        mockMvc.perform(
                        post("/firestation")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "address": "1 Test St",
                                          "station": "5"
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.address")
                        .value("1 Test St"))
                .andExpect(jsonPath("$.station")
                        .value("5"));
    }

    @Test
    void updateFireStationShouldReturnUpdatedMapping()
            throws Exception {

        FireStation fireStation =
                createFireStation("1 Test St", "2");

        when(
                fireStationService.updateFireStation(
                        any(FireStation.class)
                )
        ).thenReturn(Optional.of(fireStation));

        mockMvc.perform(
                        put("/firestation")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "address": "1 Test St",
                                          "station": "2"
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address")
                        .value("1 Test St"))
                .andExpect(jsonPath("$.station")
                        .value("2"));
    }

    @Test
    void updateFireStationShouldReturnNotFoundForUnknownAddress()
            throws Exception {

        when(
                fireStationService.updateFireStation(
                        any(FireStation.class)
                )
        ).thenReturn(Optional.empty());

        mockMvc.perform(
                        put("/firestation")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "address": "Unknown",
                                          "station": "2"
                                        }
                                        """)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json("{}"));
    }

    @Test
    void deleteFireStationShouldDeleteByAddress()
            throws Exception {

        when(
                fireStationService.deleteByAddress("1 Test St")
        ).thenReturn(true);

        mockMvc.perform(
                        delete("/firestation")
                                .param(
                                        "address",
                                        "1 Test St"
                                )
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }

    @Test
    void deleteFireStationShouldDeleteByStation()
            throws Exception {

        when(
                fireStationService.deleteByStation("3")
        ).thenReturn(true);

        mockMvc.perform(
                        delete("/firestation")
                                .param(
                                        "stationNumber",
                                        "3"
                                )
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }

    @Test
    void deleteFireStationShouldReturnNotFound()
            throws Exception {

        when(
                fireStationService.deleteByAddress("Unknown")
        ).thenReturn(false);

        mockMvc.perform(
                        delete("/firestation")
                                .param(
                                        "address",
                                        "Unknown"
                                )
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json("{}"));
    }

    @Test
    void deleteFireStationShouldReturnBadRequestWithoutParameters()
            throws Exception {

        mockMvc.perform(
                        delete("/firestation")
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{}"));
    }

    private FireStation createFireStation(
            String address,
            String station
    ) {
        FireStation fireStation = new FireStation();
        fireStation.setAddress(address);
        fireStation.setStation(station);
        return fireStation;
    }
}