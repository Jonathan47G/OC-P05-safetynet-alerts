package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.PhoneAlertService;
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

@WebMvcTest(PhoneAlertController.class)
class PhoneAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PhoneAlertService phoneAlertService;

    @Test
    void getPhoneAlertShouldReturnPhones() throws Exception {

        when(phoneAlertService.getPhonesByFirestation("3"))
                .thenReturn(List.of(
                        "111-111-1111",
                        "222-222-2222"
                ));

        mockMvc.perform(
                        get("/phoneAlert")
                                .param("firestation", "3")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                          "111-111-1111",
                          "222-222-2222"
                        ]
                        """));
    }

    @Test
    void getPhoneAlertShouldReturnEmptyListForUnknownStation()
            throws Exception {

        when(phoneAlertService.getPhonesByFirestation("999"))
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/phoneAlert")
                                .param("firestation", "999")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getPhoneAlertShouldReturnBadRequestWithoutFirestation()
            throws Exception {

        mockMvc.perform(
                        get("/phoneAlert")
                )
                .andExpect(status().isBadRequest());
    }
}