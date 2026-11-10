package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceTest {

    @Mock
    private DataRepository dataRepository;

    private MedicalRecordService medicalRecordService;

    @BeforeEach
    void setUp() {
        medicalRecordService = new MedicalRecordService(dataRepository);
    }

    @Test
    void addMedicalRecordShouldReturnAddedRecord() {

        MedicalRecord record = createMedicalRecord();

        when(dataRepository.addMedicalRecord(record))
                .thenReturn(record);

        MedicalRecord result =
                medicalRecordService.addMedicalRecord(record);

        assertEquals(record, result);

        verify(dataRepository)
                .addMedicalRecord(record);
    }

    @Test
    void updateMedicalRecordShouldReturnUpdatedRecord() {

        MedicalRecord record = createMedicalRecord();

        when(dataRepository.updateMedicalRecord(record))
                .thenReturn(Optional.of(record));

        Optional<MedicalRecord> result =
                medicalRecordService.updateMedicalRecord(record);

        assertTrue(result.isPresent());
        assertEquals(record, result.get());
    }

    @Test
    void updateMedicalRecordShouldReturnEmptyWhenRecordDoesNotExist() {

        MedicalRecord record = createMedicalRecord();

        when(dataRepository.updateMedicalRecord(record))
                .thenReturn(Optional.empty());

        Optional<MedicalRecord> result =
                medicalRecordService.updateMedicalRecord(record);

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteMedicalRecordShouldReturnTrueWhenRecordExists() {

        when(
                dataRepository.deleteMedicalRecord(
                        "Jonathan",
                        "Test"
                )
        ).thenReturn(true);

        boolean result =
                medicalRecordService.deleteMedicalRecord(
                        "Jonathan",
                        "Test"
                );

        assertTrue(result);
    }

    @Test
    void deleteMedicalRecordShouldReturnFalseWhenRecordDoesNotExist() {

        when(
                dataRepository.deleteMedicalRecord(
                        "Unknown",
                        "Person"
                )
        ).thenReturn(false);

        boolean result =
                medicalRecordService.deleteMedicalRecord(
                        "Unknown",
                        "Person"
                );

        assertFalse(result);
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