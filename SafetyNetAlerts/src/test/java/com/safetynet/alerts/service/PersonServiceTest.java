package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private DataRepository dataRepository;

    private PersonService personService;

    @BeforeEach
    void setUp() {
        personService = new PersonService(dataRepository);
    }

    @Test
    void addPersonShouldReturnAddedPerson() {

        Person person = createPerson();

        when(dataRepository.addPerson(person))
                .thenReturn(person);

        Person result = personService.addPerson(person);

        assertEquals(person, result);

        verify(dataRepository).addPerson(person);
    }

    @Test
    void updatePersonShouldReturnUpdatedPerson() {

        Person person = createPerson();

        when(dataRepository.updatePerson(person))
                .thenReturn(Optional.of(person));

        Optional<Person> result =
                personService.updatePerson(person);

        assertTrue(result.isPresent());
        assertEquals(person, result.get());

        verify(dataRepository).updatePerson(person);
    }

    @Test
    void updatePersonShouldReturnEmptyWhenPersonDoesNotExist() {

        Person person = createPerson();

        when(dataRepository.updatePerson(person))
                .thenReturn(Optional.empty());

        Optional<Person> result =
                personService.updatePerson(person);

        assertTrue(result.isEmpty());
    }

    @Test
    void deletePersonShouldReturnTrueWhenPersonExists() {

        when(dataRepository.deletePerson("John", "Doe"))
                .thenReturn(true);

        boolean result =
                personService.deletePerson("John", "Doe");

        assertTrue(result);
    }

    @Test
    void deletePersonShouldReturnFalseWhenPersonDoesNotExist() {

        when(dataRepository.deletePerson("John", "Doe"))
                .thenReturn(false);

        boolean result =
                personService.deletePerson("John", "Doe");

        assertFalse(result);
    }

    private Person createPerson() {

        Person person = new Person();

        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("1 Test St");
        person.setCity("Culver");
        person.setZip("97451");
        person.setPhone("111-222-3333");
        person.setEmail("john@test.com");

        return person;
    }
}