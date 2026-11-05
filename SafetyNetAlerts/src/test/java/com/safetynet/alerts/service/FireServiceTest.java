package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FireResponseDto;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FireServiceTest {

    @Mock
    private DataRepository dataRepository;

    @Mock
    private AgeCalculator ageCalculator;

    private FireService fireService;

    @BeforeEach
    void setUp() {
        fireService = new FireService(
                dataRepository,
                ageCalculator
        );
    }

    @Test
    void getPersonsByAddressShouldReturnFireInformation() {

        FireStation fireStation =
                createFireStation("1509 Culver St", "3");

        Person person =
                createPerson(
                        "John",
                        "Boyd",
                        "1509 Culver St",
                        "841-874-6512"
                );

        MedicalRecord record =
                createMedicalRecord(
                        "John",
                        "Boyd",
                        "03/06/1984",
                        List.of("aznol:350mg"),
                        List.of("nillacilan")
                );

        when(dataRepository.getFirestations())
                .thenReturn(List.of(fireStation));

        when(dataRepository.getPersons())
                .thenReturn(List.of(person));

        when(dataRepository.getMedicalRecords())
                .thenReturn(List.of(record));

        when(ageCalculator.calculateAge("03/06/1984"))
                .thenReturn(42);

        Optional<FireResponseDto> result =
                fireService.getPersonsByAddress(
                        "1509 Culver St"
                );

        assertTrue(result.isPresent());

        FireResponseDto response = result.get();

        assertEquals("3", response.stationNumber());
        assertEquals(1, response.persons().size());

        assertEquals(
                "John",
                response.persons().getFirst().firstName()
        );

        assertEquals(
                42,
                response.persons().getFirst().age()
        );

        assertEquals(
                List.of("aznol:350mg"),
                response.persons().getFirst().medications()
        );

        assertEquals(
                List.of("nillacilan"),
                response.persons().getFirst().allergies()
        );
    }

    @Test
    void getPersonsByAddressShouldReturnEmptyForUnknownAddress() {

        when(dataRepository.getFirestations())
                .thenReturn(List.of());

        Optional<FireResponseDto> result =
                fireService.getPersonsByAddress(
                        "Unknown address"
                );

        assertTrue(result.isEmpty());
    }

    @Test
    void getPersonsByAddressShouldIgnorePersonWithoutMedicalRecord() {

        FireStation fireStation =
                createFireStation("1509 Culver St", "3");

        Person person =
                createPerson(
                        "John",
                        "Boyd",
                        "1509 Culver St",
                        "841-874-6512"
                );

        when(dataRepository.getFirestations())
                .thenReturn(List.of(fireStation));

        when(dataRepository.getPersons())
                .thenReturn(List.of(person));

        when(dataRepository.getMedicalRecords())
                .thenReturn(List.of());

        Optional<FireResponseDto> result =
                fireService.getPersonsByAddress(
                        "1509 Culver St"
                );

        assertTrue(result.isPresent());
        assertTrue(result.get().persons().isEmpty());
    }

    @Test
    void getPersonsByAddressShouldIgnoreCase() {

        FireStation fireStation =
                createFireStation("1509 Culver St", "3");

        when(dataRepository.getFirestations())
                .thenReturn(List.of(fireStation));

        when(dataRepository.getPersons())
                .thenReturn(List.of());

        Optional<FireResponseDto> result =
                fireService.getPersonsByAddress(
                        "1509 culver st"
                );

        assertTrue(result.isPresent());
        assertEquals("3", result.get().stationNumber());
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
            String birthdate,
            List<String> medications,
            List<String> allergies
    ) {
        MedicalRecord record = new MedicalRecord();
        record.setFirstName(firstName);
        record.setLastName(lastName);
        record.setBirthdate(birthdate);
        record.setMedications(medications);
        record.setAllergies(allergies);
        return record;
    }
}