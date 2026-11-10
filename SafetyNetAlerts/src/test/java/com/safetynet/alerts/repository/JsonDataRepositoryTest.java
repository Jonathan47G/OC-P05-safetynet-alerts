package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JsonDataRepositoryTest {

    @Autowired
    private JsonDataRepository repository;

    @BeforeEach
    void setUp() {
        // Recharge le JSON avant chaque test afin que les tests
        // restent indépendants les uns des autres.
        repository.loadData();
    }

    @Test
    void loadDataShouldLoadSafetyNetData() {

        assertNotNull(repository.getPersons());
        assertNotNull(repository.getFirestations());
        assertNotNull(repository.getMedicalRecords());

        assertFalse(repository.getPersons().isEmpty());
        assertFalse(repository.getFirestations().isEmpty());
        assertFalse(repository.getMedicalRecords().isEmpty());
    }

    @Test
    void addPersonShouldAddPerson() {

        Person person = createPerson();

        int initialSize = repository.getPersons().size();

        Person result = repository.addPerson(person);

        assertEquals(person, result);
        assertEquals(
                initialSize + 1,
                repository.getPersons().size()
        );

        assertTrue(
                repository.getPersons().contains(person)
        );
    }

    @Test
    void updatePersonShouldUpdateExistingPerson() {

        Person person = createPerson();

        repository.addPerson(person);

        Person updatedPerson = new Person();
        updatedPerson.setFirstName("Jonathan");
        updatedPerson.setLastName("Test");
        updatedPerson.setAddress("42 New Address");
        updatedPerson.setCity("New City");
        updatedPerson.setZip("12345");
        updatedPerson.setPhone("999-999-9999");
        updatedPerson.setEmail("updated@test.com");

        Optional<Person> result =
                repository.updatePerson(updatedPerson);

        assertTrue(result.isPresent());

        Person updated = result.get();

        assertEquals("Jonathan", updated.getFirstName());
        assertEquals("Test", updated.getLastName());

        assertEquals(
                "42 New Address",
                updated.getAddress()
        );

        assertEquals(
                "New City",
                updated.getCity()
        );

        assertEquals(
                "12345",
                updated.getZip()
        );

        assertEquals(
                "999-999-9999",
                updated.getPhone()
        );

        assertEquals(
                "updated@test.com",
                updated.getEmail()
        );
    }

    @Test
    void updatePersonShouldReturnEmptyWhenPersonDoesNotExist() {

        Person person = createPerson();

        Optional<Person> result =
                repository.updatePerson(person);

        assertTrue(result.isEmpty());
    }

    @Test
    void deletePersonShouldDeleteExistingPerson() {

        Person person = createPerson();

        repository.addPerson(person);

        boolean result =
                repository.deletePerson(
                        "Jonathan",
                        "Test"
                );

        assertTrue(result);

        assertTrue(
                repository.getPersons()
                        .stream()
                        .noneMatch(existing ->
                                existing.getFirstName()
                                        .equals("Jonathan")
                                        &&
                                        existing.getLastName()
                                                .equals("Test")
                        )
        );
    }

    @Test
    void deletePersonShouldReturnFalseWhenPersonDoesNotExist() {

        boolean result =
                repository.deletePerson(
                        "Unknown",
                        "Person"
                );

        assertFalse(result);
    }

    @Test
    void updatePersonShouldIgnoreCaseForIdentity() {

        Person person = createPerson();

        repository.addPerson(person);

        Person updatedPerson = createPerson();
        updatedPerson.setFirstName("jonathan");
        updatedPerson.setLastName("test");
        updatedPerson.setAddress("Case Insensitive Address");

        Optional<Person> result =
                repository.updatePerson(updatedPerson);

        assertTrue(result.isPresent());

        assertEquals(
                "Case Insensitive Address",
                result.get().getAddress()
        );
    }

    private Person createPerson() {

        Person person = new Person();

        person.setFirstName("Jonathan");
        person.setLastName("Test");
        person.setAddress("1 Test St");
        person.setCity("Culver");
        person.setZip("97451");
        person.setPhone("111-222-3333");
        person.setEmail("jonathan@test.com");

        return person;
    }

    @Test
    void addFireStationShouldAddMapping() {

        FireStation fireStation =
                createFireStation("1 Test St", "5");

        int initialSize =
                repository.getFirestations().size();

        FireStation result =
                repository.addFireStation(fireStation);

        assertEquals(fireStation, result);

        assertEquals(
                initialSize + 1,
                repository.getFirestations().size()
        );
    }

    @Test
    void updateFireStationShouldUpdateStationNumber() {

        FireStation fireStation =
                createFireStation("1 Test St", "5");

        repository.addFireStation(fireStation);

        FireStation update =
                createFireStation("1 Test St", "2");

        Optional<FireStation> result =
                repository.updateFireStation(update);

        assertTrue(result.isPresent());
        assertEquals("2", result.get().getStation());
    }

    @Test
    void updateFireStationShouldReturnEmptyForUnknownAddress() {

        FireStation update =
                createFireStation("Unknown", "2");

        assertTrue(
                repository.updateFireStation(update)
                        .isEmpty()
        );
    }

    @Test
    void deleteFireStationByAddressShouldDeleteMapping() {

        repository.addFireStation(
                createFireStation("1 Test St", "5")
        );

        assertTrue(
                repository.deleteFireStationByAddress(
                        "1 Test St"
                )
        );
    }

    @Test
    void deleteFireStationsByStationShouldDeleteMappings() {

        repository.addFireStation(
                createFireStation("1 Test St", "99")
        );

        repository.addFireStation(
                createFireStation("2 Test St", "99")
        );

        assertTrue(
                repository.deleteFireStationsByStation("99")
        );

        assertTrue(
                repository.getFirestations()
                        .stream()
                        .noneMatch(
                                fireStation ->
                                        fireStation.getStation()
                                                .equals("99")
                        )
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
    @Test
    void addMedicalRecordShouldAddRecord() {

        MedicalRecord record =
                createMedicalRecord();

        int initialSize =
                repository.getMedicalRecords().size();

        MedicalRecord result =
                repository.addMedicalRecord(record);

        assertEquals(record, result);

        assertEquals(
                initialSize + 1,
                repository.getMedicalRecords().size()
        );

        assertTrue(
                repository.getMedicalRecords()
                        .contains(record)
        );
    }

    @Test
    void updateMedicalRecordShouldUpdateExistingRecord() {

        MedicalRecord record =
                createMedicalRecord();

        repository.addMedicalRecord(record);

        MedicalRecord updated =
                new MedicalRecord();

        updated.setFirstName("Jonathan");
        updated.setLastName("Test");
        updated.setBirthdate("02/02/1990");

        updated.setMedications(
                List.of("newmed:250mg")
        );

        updated.setAllergies(
                List.of("shellfish")
        );

        Optional<MedicalRecord> result =
                repository.updateMedicalRecord(updated);

        assertTrue(result.isPresent());

        MedicalRecord resultRecord =
                result.get();

        assertEquals(
                "Jonathan",
                resultRecord.getFirstName()
        );

        assertEquals(
                "Test",
                resultRecord.getLastName()
        );

        assertEquals(
                "02/02/1990",
                resultRecord.getBirthdate()
        );

        assertEquals(
                List.of("newmed:250mg"),
                resultRecord.getMedications()
        );

        assertEquals(
                List.of("shellfish"),
                resultRecord.getAllergies()
        );
    }

    @Test
    void updateMedicalRecordShouldReturnEmptyWhenRecordDoesNotExist() {

        MedicalRecord record =
                createMedicalRecord();

        Optional<MedicalRecord> result =
                repository.updateMedicalRecord(record);

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteMedicalRecordShouldDeleteExistingRecord() {

        MedicalRecord record =
                createMedicalRecord();

        repository.addMedicalRecord(record);

        boolean deleted =
                repository.deleteMedicalRecord(
                        "Jonathan",
                        "Test"
                );

        assertTrue(deleted);

        assertTrue(
                repository.getMedicalRecords()
                        .stream()
                        .noneMatch(existing ->
                                existing.getFirstName()
                                        .equalsIgnoreCase(
                                                "Jonathan"
                                        )
                                        &&
                                        existing.getLastName()
                                                .equalsIgnoreCase(
                                                        "Test"
                                                )
                        )
        );
    }

    @Test
    void deleteMedicalRecordShouldReturnFalseWhenRecordDoesNotExist() {

        boolean deleted =
                repository.deleteMedicalRecord(
                        "Unknown",
                        "Person"
                );

        assertFalse(deleted);
    }
}