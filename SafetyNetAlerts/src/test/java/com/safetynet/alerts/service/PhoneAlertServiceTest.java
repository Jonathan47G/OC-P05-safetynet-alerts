package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
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
class PhoneAlertServiceTest {

    @Mock
    private DataRepository dataRepository;

    private PhoneAlertService phoneAlertService;

    @BeforeEach
    void setUp() {
        phoneAlertService = new PhoneAlertService(dataRepository);
    }

    @Test
    void getPhonesByFirestationShouldReturnMatchingPhones() {

        FireStation station = createFireStation("1509 Culver St", "3");

        Person john = createPerson("1509 Culver St", "111-111-1111");
        Person jane = createPerson("1509 Culver St", "222-222-2222");

        when(dataRepository.getFirestations())
                .thenReturn(List.of(station));

        when(dataRepository.getPersons())
                .thenReturn(List.of(john, jane));

        List<String> result =
                phoneAlertService.getPhonesByFirestation("3");

        assertEquals(2, result.size());
        assertTrue(result.contains("111-111-1111"));
        assertTrue(result.contains("222-222-2222"));
    }

    @Test
    void getPhonesByFirestationShouldReturnEmptyListForUnknownStation() {

        when(dataRepository.getFirestations())
                .thenReturn(List.of(
                        createFireStation("1509 Culver St", "3")
                ));

        when(dataRepository.getPersons())
                .thenReturn(List.of(
                        createPerson("1509 Culver St", "111-111-1111")
                ));

        List<String> result =
                phoneAlertService.getPhonesByFirestation("999");

        assertTrue(result.isEmpty());
    }

    @Test
    void getPhonesByFirestationShouldRemoveDuplicatePhones() {

        FireStation station = createFireStation("1509 Culver St", "3");

        Person john = createPerson("1509 Culver St", "111-111-1111");
        Person jane = createPerson("1509 Culver St", "111-111-1111");

        when(dataRepository.getFirestations())
                .thenReturn(List.of(station));

        when(dataRepository.getPersons())
                .thenReturn(List.of(john, jane));

        List<String> result =
                phoneAlertService.getPhonesByFirestation("3");

        assertEquals(1, result.size());
        assertEquals("111-111-1111", result.getFirst());
    }

    @Test
    void getPhonesByFirestationShouldExcludePeopleFromOtherAddresses() {

        FireStation station = createFireStation("1509 Culver St", "3");

        Person coveredPerson =
                createPerson("1509 Culver St", "111-111-1111");

        Person otherPerson =
                createPerson("29 15th St", "222-222-2222");

        when(dataRepository.getFirestations())
                .thenReturn(List.of(station));

        when(dataRepository.getPersons())
                .thenReturn(List.of(coveredPerson, otherPerson));

        List<String> result =
                phoneAlertService.getPhonesByFirestation("3");

        assertEquals(
                List.of("111-111-1111"),
                result
        );
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
            String address,
            String phone
    ) {
        Person person = new Person();
        person.setAddress(address);
        person.setPhone(phone);

        return person;
    }
}