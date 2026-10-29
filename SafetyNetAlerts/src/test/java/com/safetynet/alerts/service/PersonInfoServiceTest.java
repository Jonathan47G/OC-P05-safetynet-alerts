package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonInfoDto;
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
class PersonInfoServiceTest {

    @Mock
    private DataRepository dataRepository;

    @Mock
    private AgeCalculator ageCalculator;

    private PersonInfoService personInfoService;

    @BeforeEach
    void setUp() {
        personInfoService = new PersonInfoService(
                dataRepository,
                ageCalculator
        );
    }

    @Test
    void getPersonsByLastNameShouldReturnMatchingPersons() {

        Person john = createPerson(
                "John",
                "Boyd",
                "1509 Culver St",
                "john@email.com"
        );

        Person tenley = createPerson(
                "Tenley",
                "Boyd",
                "1509 Culver St",
                "tenley@email.com"
        );

        MedicalRecord johnRecord = createMedicalRecord(
                "John",
                "Boyd",
                "03/06/1984"
        );

        MedicalRecord tenleyRecord = createMedicalRecord(
                "Tenley",
                "Boyd",
                "02/18/2012"
        );

        when(dataRepository.getPersons())
                .thenReturn(List.of(john, tenley));

        when(dataRepository.getMedicalRecords())
                .thenReturn(List.of(johnRecord, tenleyRecord));

        when(ageCalculator.calculateAge("03/06/1984"))
                .thenReturn(42);

        when(ageCalculator.calculateAge("02/18/2012"))
                .thenReturn(14);

        List<PersonInfoDto> result =
                personInfoService.getPersonsByLastName("Boyd");

        assertEquals(2, result.size());

        assertEquals("John", result.get(0).firstName());
        assertEquals(42, result.get(0).age());

        assertEquals("Tenley", result.get(1).firstName());
        assertEquals(14, result.get(1).age());
    }

    @Test
    void getPersonsByLastNameShouldIgnoreCase() {

        Person john = createPerson(
                "John",
                "Boyd",
                "1509 Culver St",
                "john@email.com"
        );

        MedicalRecord record = createMedicalRecord(
                "John",
                "Boyd",
                "03/06/1984"
        );

        when(dataRepository.getPersons())
                .thenReturn(List.of(john));

        when(dataRepository.getMedicalRecords())
                .thenReturn(List.of(record));

        when(ageCalculator.calculateAge("03/06/1984"))
                .thenReturn(42);

        List<PersonInfoDto> result =
                personInfoService.getPersonsByLastName("boyd");

        assertEquals(1, result.size());
    }

    @Test
    void getPersonsByLastNameShouldReturnEmptyListForUnknownName() {

        when(dataRepository.getPersons())
                .thenReturn(List.of());

        List<PersonInfoDto> result =
                personInfoService.getPersonsByLastName("Unknown");

        assertTrue(result.isEmpty());
    }

    @Test
    void getPersonsByLastNameShouldIgnorePersonWithoutMedicalRecord() {

        Person john = createPerson(
                "John",
                "Boyd",
                "1509 Culver St",
                "john@email.com"
        );

        when(dataRepository.getPersons())
                .thenReturn(List.of(john));

        when(dataRepository.getMedicalRecords())
                .thenReturn(List.of());

        List<PersonInfoDto> result =
                personInfoService.getPersonsByLastName("Boyd");

        assertTrue(result.isEmpty());
    }

    private Person createPerson(
            String firstName,
            String lastName,
            String address,
            String email
    ) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setAddress(address);
        person.setEmail(email);

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