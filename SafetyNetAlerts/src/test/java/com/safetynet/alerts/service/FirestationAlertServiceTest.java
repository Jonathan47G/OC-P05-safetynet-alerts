package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FirestationResponseDto;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import com.safetynet.alerts.util.AgeCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FirestationAlertServiceTest {

    @Mock
    private DataRepository dataRepository;

    @Mock
    private AgeCalculator ageCalculator;

    private FirestationAlertService firestationAlertService;

    @BeforeEach
    void setUp() {
        firestationAlertService = new FirestationAlertService(
                dataRepository,
                ageCalculator
        );
    }

    @Test
    void getPersonsByStationShouldReturnCoveredPersonsAndCounts() {

        FireStation fireStation =
                createFireStation("1509 Culver St", "3");

        Person adult =
                createPerson(
                        "John",
                        "Boyd",
                        "1509 Culver St",
                        "111-111-1111"
                );

        Person child =
                createPerson(
                        "Tenley",
                        "Boyd",
                        "1509 Culver St",
                        "222-222-2222"
                );

        MedicalRecord adultRecord =
                createMedicalRecord(
                        "John",
                        "Boyd",
                        "03/06/1984"
                );

        MedicalRecord childRecord =
                createMedicalRecord(
                        "Tenley",
                        "Boyd",
                        "02/18/2012"
                );

        when(dataRepository.getFirestations())
                .thenReturn(List.of(fireStation));

        when(dataRepository.getPersons())
                .thenReturn(List.of(adult, child));

        when(dataRepository.getMedicalRecords())
                .thenReturn(List.of(adultRecord, childRecord));

        when(ageCalculator.calculateAge("03/06/1984"))
                .thenReturn(42);

        when(ageCalculator.calculateAge("02/18/2012"))
                .thenReturn(14);

        FirestationResponseDto result =
                firestationAlertService.getPersonsByStation("3");

        assertEquals(2, result.persons().size());
        assertEquals(1, result.adultCount());
        assertEquals(1, result.childCount());

        assertEquals(
                "John",
                result.persons().getFirst().firstName()
        );
    }

    @Test
    void getPersonsByStationShouldIncludeEighteenYearOldAsChild() {

        FireStation fireStation =
                createFireStation("1 Test St", "1");

        Person person =
                createPerson(
                        "Alex",
                        "Test",
                        "1 Test St",
                        "111-111-1111"
                );

        MedicalRecord record =
                createMedicalRecord(
                        "Alex",
                        "Test",
                        "01/01/2008"
                );

        when(dataRepository.getFirestations())
                .thenReturn(List.of(fireStation));

        when(dataRepository.getPersons())
                .thenReturn(List.of(person));

        when(dataRepository.getMedicalRecords())
                .thenReturn(List.of(record));

        when(ageCalculator.calculateAge("01/01/2008"))
                .thenReturn(18);

        FirestationResponseDto result =
                firestationAlertService.getPersonsByStation("1");

        assertEquals(0, result.adultCount());
        assertEquals(1, result.childCount());
    }

    @Test
    void getPersonsByStationShouldReturnEmptyResponseForUnknownStation() {

        when(dataRepository.getFirestations())
                .thenReturn(List.of());

        when(dataRepository.getPersons())
                .thenReturn(List.of());

        FirestationResponseDto result =
                firestationAlertService.getPersonsByStation("999");

        assertTrue(result.persons().isEmpty());
        assertEquals(0, result.adultCount());
        assertEquals(0, result.childCount());
    }

    @Test
    void getPersonsByStationShouldIgnorePersonWithoutMedicalRecordForCounts() {

        FireStation fireStation =
                createFireStation("1509 Culver St", "3");

        Person person =
                createPerson(
                        "John",
                        "Boyd",
                        "1509 Culver St",
                        "111-111-1111"
                );

        when(dataRepository.getFirestations())
                .thenReturn(List.of(fireStation));

        when(dataRepository.getPersons())
                .thenReturn(List.of(person));

        when(dataRepository.getMedicalRecords())
                .thenReturn(List.of());

        FirestationResponseDto result =
                firestationAlertService.getPersonsByStation("3");

        assertEquals(1, result.persons().size());

        assertEquals(0, result.adultCount());
        assertEquals(0, result.childCount());
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

    private Person createPerson(
            String firstName,
            String lastName,
            String address,
            String phone
    ) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setAddress(address);
        person.setPhone(phone);

        return person;
    }

    private MedicalRecord createMedicalRecord(
            String firstName,
            String lastName,
            String birthdate
    ) {
        MedicalRecord record = new MedicalRecord();
        record.setFirstName(firstName);
        record.setLastName(lastName);
        record.setBirthdate(birthdate);

        return record;
    }
}