package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.ChildAlertDto;
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
class ChildAlertServiceTest {

    @Mock
    private DataRepository dataRepository;

    @Mock
    private AgeCalculator ageCalculator;

    private ChildAlertService childAlertService;

    @BeforeEach
    void setUp() {
        childAlertService = new ChildAlertService(
                dataRepository,
                ageCalculator
        );
    }

    @Test
    void getChildrenByAddressShouldReturnChildren() {

        Person child = createPerson(
                "Tenley",
                "Boyd",
                "1509 Culver St"
        );

        Person adult = createPerson(
                "John",
                "Boyd",
                "1509 Culver St"
        );

        MedicalRecord childRecord = createMedicalRecord(
                "Tenley",
                "Boyd",
                "02/18/2012"
        );

        MedicalRecord adultRecord = createMedicalRecord(
                "John",
                "Boyd",
                "03/06/1984"
        );

        when(dataRepository.getPersons())
                .thenReturn(List.of(child, adult));

        when(dataRepository.getMedicalRecords())
                .thenReturn(List.of(childRecord, adultRecord));

        when(ageCalculator.calculateAge("02/18/2012"))
                .thenReturn(14);

        when(ageCalculator.calculateAge("03/06/1984"))
                .thenReturn(42);

        List<ChildAlertDto> result =
                childAlertService.getChildrenByAddress(
                        "1509 Culver St"
                );

        assertEquals(1, result.size());

        ChildAlertDto childResult = result.getFirst();

        assertEquals("Tenley", childResult.firstName());
        assertEquals("Boyd", childResult.lastName());
        assertEquals(14, childResult.age());

        assertEquals(
                1,
                childResult.householdMembers().size()
        );

        assertEquals(
                "John",
                childResult.householdMembers()
                        .getFirst()
                        .firstName()
        );
    }

    @Test
    void getChildrenByAddressShouldIncludeEighteenYearOld() {

        Person person = createPerson(
                "Alex",
                "Test",
                "1 Test St"
        );

        MedicalRecord record = createMedicalRecord(
                "Alex",
                "Test",
                "01/01/2008"
        );

        when(dataRepository.getPersons())
                .thenReturn(List.of(person));

        when(dataRepository.getMedicalRecords())
                .thenReturn(List.of(record));

        when(ageCalculator.calculateAge("01/01/2008"))
                .thenReturn(18);

        List<ChildAlertDto> result =
                childAlertService.getChildrenByAddress(
                        "1 Test St"
                );

        assertEquals(1, result.size());
        assertEquals(18, result.getFirst().age());
    }

    @Test
    void getChildrenByAddressShouldExcludeAdults() {

        Person person = createPerson(
                "John",
                "Boyd",
                "1509 Culver St"
        );

        MedicalRecord record = createMedicalRecord(
                "John",
                "Boyd",
                "03/06/1984"
        );

        when(dataRepository.getPersons())
                .thenReturn(List.of(person));

        when(dataRepository.getMedicalRecords())
                .thenReturn(List.of(record));

        when(ageCalculator.calculateAge("03/06/1984"))
                .thenReturn(42);

        List<ChildAlertDto> result =
                childAlertService.getChildrenByAddress(
                        "1509 Culver St"
                );

        assertTrue(result.isEmpty());
    }

    @Test
    void getChildrenByAddressShouldReturnEmptyListForUnknownAddress() {

        when(dataRepository.getPersons())
                .thenReturn(List.of());

        List<ChildAlertDto> result =
                childAlertService.getChildrenByAddress(
                        "Unknown address"
                );

        assertTrue(result.isEmpty());
    }

    @Test
    void getChildrenByAddressShouldIgnorePersonWithoutMedicalRecord() {

        Person person = createPerson(
                "Unknown",
                "Person",
                "1 Test St"
        );

        when(dataRepository.getPersons())
                .thenReturn(List.of(person));

        when(dataRepository.getMedicalRecords())
                .thenReturn(List.of());

        List<ChildAlertDto> result =
                childAlertService.getChildrenByAddress(
                        "1 Test St"
                );

        assertTrue(result.isEmpty());
    }

    private Person createPerson(
            String firstName,
            String lastName,
            String address
    ) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setAddress(address);

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