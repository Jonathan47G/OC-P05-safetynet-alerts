package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}