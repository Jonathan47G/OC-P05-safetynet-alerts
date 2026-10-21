package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.CommunityEmailService;
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

@WebMvcTest(CommunityEmailController.class)
class CommunityEmailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommunityEmailService communityEmailService;

    @Test
    void getCommunityEmailsShouldReturnEmails() throws Exception {

        when(communityEmailService.getEmailsByCity("Culver"))
                .thenReturn(List.of(
                        "john@email.com",
                        "anna@email.com"
                ));

        mockMvc.perform(
                        get("/communityEmail")
                                .param("city", "Culver")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                          "john@email.com",
                          "anna@email.com"
                        ]
                        """));
    }

    @Test
    void getCommunityEmailsShouldReturnEmptyListForUnknownCity()
            throws Exception {

        when(communityEmailService.getEmailsByCity("Unknown"))
                .thenReturn(List.of());

        mockMvc.perform(
                        get("/communityEmail")
                                .param("city", "Unknown")
                )
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getCommunityEmailsShouldReturnBadRequestWithoutCity()
            throws Exception {

        mockMvc.perform(
                        get("/communityEmail")
                )
                .andExpect(status().isBadRequest());
    }
}