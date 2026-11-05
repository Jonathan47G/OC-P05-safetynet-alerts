package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FloodHouseholdDto;
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
class FloodServiceTest {

    @Mock
    private DataRepository dataRepository;

    @Mock
    private AgeCalculator ageCalculator;

    private FloodService floodService;

    @BeforeEach
    void setUp() {
        floodService = new FloodService(
                dataRepository,
                ageCalculator
        );
    }

    @Test
    void getHouseholdsByStationsShouldReturnGroupedHouseholds() {

        FireStation station1 =
                createFireStation("1509 Culver St", "3");

        FireStation station2 =
                createFireStation("29 15th St", "2");

        Person john =
                createPerson(
                        "John",
                        "Boyd",
                        "1509 Culver St",
                        "111-111-1111"
                );

        Person jonanathan =
                createPerson(
                        "Jonanathan",
                        "Marrack",
                        "29 15th St",
                        "222-222-2222"
                );

        MedicalRecord johnRecord =
                createMedicalRecord(
                        "John",
                        "Boyd",
                        "03/06/1984"
                );

        MedicalRecord jonanathanRecord =
                createMedicalRecord(
                        "Jonanathan",
                        "Marrack",
                        "01/03/1989"
                );

        when(dataRepository.getFirestations())
                .thenReturn(List.of(station1, station2));

        when(dataRepository.getPersons())
                .thenReturn(List.of(john, jonanathan));

        when(dataRepository.getMedicalRecords())
                .thenReturn(List.of(
                        johnRecord,
                        jonanathanRecord
                ));

        when(ageCalculator.calculateAge("03/06/1984"))
                .thenReturn(42);

        when(ageCalculator.calculateAge("01/03/1989"))
                .thenReturn(37);

        List<FloodHouseholdDto> result =
                floodService.getHouseholdsByStations(
                        List.of("2", "3")
                );

        assertEquals(2, result.size());

        assertTrue(
                result.stream()
                        .anyMatch(household ->
                                household.address()
                                        .equals("1509 Culver St"))
        );

        assertTrue(
                result.stream()
                        .anyMatch(household ->
                                household.address()
                                        .equals("29 15th St"))
        );
    }

    @Test
    void getHouseholdsByStationsShouldReturnEmptyListForUnknownStation() {

        when(dataRepository.getFirestations())
                .thenReturn(List.of());

        List<FloodHouseholdDto> result =
                floodService.getHouseholdsByStations(
                        List.of("999")
                );

        assertTrue(result.isEmpty());
    }

    @Test
    void getHouseholdsByStationsShouldRemoveDuplicateAddresses() {

        FireStation first =
                createFireStation("748 Townings Dr", "3");

        FireStation duplicate =
                createFireStation("748 Townings Dr", "3");

        Person person =
                createPerson(
                        "Foster",
                        "Shepard",
                        "748 Townings Dr",
                        "111-111-1111"
                );

        MedicalRecord record =
                createMedicalRecord(
                        "Foster",
                        "Shepard",
                        "01/08/1980"
                );

        when(dataRepository.getFirestations())
                .thenReturn(List.of(first, duplicate));

        when(dataRepository.getPersons())
                .thenReturn(List.of(person));

        when(dataRepository.getMedicalRecords())
                .thenReturn(List.of(record));

        when(ageCalculator.calculateAge("01/08/1980"))
                .thenReturn(46);

        List<FloodHouseholdDto> result =
                floodService.getHouseholdsByStations(
                        List.of("3")
                );

        assertEquals(1, result.size());
        assertEquals(
                "748 Townings Dr",
                result.getFirst().address()
        );
    }

    @Test
    void getHouseholdsByStationsShouldIgnorePersonWithoutMedicalRecord() {

        FireStation station =
                createFireStation("1509 Culver St", "3");

        Person person =
                createPerson(
                        "John",
                        "Boyd",
                        "1509 Culver St",
                        "111-111-1111"
                );

        when(dataRepository.getFirestations())
                .thenReturn(List.of(station));

        when(dataRepository.getPersons())
                .thenReturn(List.of(person));

        when(dataRepository.getMedicalRecords())
                .thenReturn(List.of());

        List<FloodHouseholdDto> result =
                floodService.getHouseholdsByStations(
                        List.of("3")
                );

        assertTrue(result.isEmpty());
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
        record.setMedications(List.of("test:100mg"));
        record.setAllergies(List.of("peanut"));
        return record;
    }
}