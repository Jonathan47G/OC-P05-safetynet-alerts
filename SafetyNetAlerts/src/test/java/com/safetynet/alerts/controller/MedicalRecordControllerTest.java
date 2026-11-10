package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicalRecordController.class)
class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MedicalRecordService medicalRecordService;

    @Test
    void addMedicalRecordShouldReturnCreatedRecord()
            throws Exception {

        MedicalRecord record = createMedicalRecord();

        when(
                medicalRecordService.addMedicalRecord(
                        any(MedicalRecord.class)
                )
        ).thenReturn(record);

        mockMvc.perform(
                        post("/medicalRecord")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "firstName": "Jonathan",
                                          "lastName": "Test",
                                          "birthdate": "01/01/1990",
                                          "medications": [
                                            "testol:100mg"
                                          ],
                                          "allergies": [
                                            "peanut"
                                          ]
                                        }
                                        """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName")
                        .value("Jonathan"))
                .andExpect(jsonPath("$.lastName")
                        .value("Test"))
                .andExpect(jsonPath("$.birthdate")
                        .value("01/01/1990"))
                .andExpect(jsonPath("$.medications[0]")
                        .value("testol:100mg"))
                .andExpect(jsonPath("$.allergies[0]")
                        .value("peanut"));
    }

    @Test
    void updateMedicalRecordShouldReturnUpdatedRecord()
            throws Exception {

        MedicalRecord record = createMedicalRecord();

        record.setMedications(
                List.of("newmed:250mg")
        );

        record.setAllergies(
                List.of()
        );

        when(
                medicalRecordService.updateMedicalRecord(
                        any(MedicalRecord.class)
                )
        ).thenReturn(Optional.of(record));

        mockMvc.perform(
                        put("/medicalRecord")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "firstName": "Jonathan",
                                          "lastName": "Test",
                                          "birthdate": "01/01/1990",
                                          "medications": [
                                            "newmed:250mg"
                                          ],
                                          "allergies": []
                                        }
                                        """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.medications[0]")
                        .value("newmed:250mg"))
                .andExpect(jsonPath("$.allergies")
                        .isEmpty());
    }

    @Test
    void updateMedicalRecordShouldReturnNotFoundWhenRecordDoesNotExist()
            throws Exception {

        when(
                medicalRecordService.updateMedicalRecord(
                        any(MedicalRecord.class)
                )
        ).thenReturn(Optional.empty());

        mockMvc.perform(
                        put("/medicalRecord")
                                .contentType("application/json")
                                .content("""
                                        {
                                          "firstName": "Unknown",
                                          "lastName": "Person",
                                          "birthdate": "01/01/1990",
                                          "medications": [],
                                          "allergies": []
                                        }
                                        """)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json("{}"));
    }

    @Test
    void deleteMedicalRecordShouldReturnOkWhenRecordExists()
            throws Exception {

        when(
                medicalRecordService.deleteMedicalRecord(
                        "Jonathan",
                        "Test"
                )
        ).thenReturn(true);

        mockMvc.perform(
                        delete("/medicalRecord")
                                .param(
                                        "firstName",
                                        "Jonathan"
                                )
                                .param(
                                        "lastName",
                                        "Test"
                                )
                )
                .andExpect(status().isOk())
                .andExpect(content().json("{}"));
    }

    @Test
    void deleteMedicalRecordShouldReturnNotFoundWhenRecordDoesNotExist()
            throws Exception {

        when(
                medicalRecordService.deleteMedicalRecord(
                        "Unknown",
                        "Person"
                )
        ).thenReturn(false);

        mockMvc.perform(
                        delete("/medicalRecord")
                                .param(
                                        "firstName",
                                        "Unknown"
                                )
                                .param(
                                        "lastName",
                                        "Person"
                                )
                )
                .andExpect(status().isNotFound())
                .andExpect(content().json("{}"));
    }

    @Test
    void deleteMedicalRecordShouldReturnBadRequestWithoutParameters()
            throws Exception {

        mockMvc.perform(
                        delete("/medicalRecord")
                )
                .andExpect(status().isBadRequest());
    }

    private MedicalRecord createMedicalRecord() {

        MedicalRecord record = new MedicalRecord();

        record.setFirstName("Jonathan");
        record.setLastName("Test");
        record.setBirthdate("01/01/1990");

        record.setMedications(
                List.of("testol:100mg")
        );

        record.setAllergies(
                List.of("peanut")
        );

        return record;
    }
}