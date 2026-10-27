package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.ChildAlertDto;
import com.safetynet.alerts.dto.HouseholdMemberDto;
import com.safetynet.alerts.service.ChildAlertService;
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

@WebMvcTest(ChildAlertController.class)
class ChildAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ChildAlertService childAlertService;

    @Test
    void getChildAlertShouldReturnChildren() throws Exception {

        ChildAlertDto child = new ChildAlertDto(
                "Tenley",
                "Boyd",
                14,
                List.of(
                        new HouseholdMemberDto(
                                "John",
                                "Boyd"
                        )
                )
        );

        when(childAlertService.getChildrenByAddress(
                "1509 Culver St"
        )).thenReturn(List.of(child));

        mockMvc.perform(
                        get("/childAlert")
                                .param(
                                        "address",
                                        "1509 Culver St"
                                )
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                          {
                            "firstName": "Tenley",
                            "lastName": "Boyd",
                            "age": 14,
                            "householdMembers": [
                              {
                                "firstName": "John",
                                "lastName": "Boyd"
                              }
                            ]
                          }
                        ]
                        """));
    }

    @Test
    void getChildAlertShouldReturnEmptyListForUnknownAddress()
            throws Exception {

        when(childAlertService.getChildrenByAddress(
                "Unknown"
        )).thenReturn(List.of());

        mockMvc.perform(
                        get("/childAlert")
                                .param("address", "Unknown")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getChildAlertShouldReturnBadRequestWithoutAddress()
            throws Exception {

        mockMvc.perform(
                        get("/childAlert")
                )
                .andExpect(status().isBadRequest());
    }
}