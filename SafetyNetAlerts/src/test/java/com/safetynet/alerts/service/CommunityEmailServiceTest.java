package com.safetynet.alerts.service;

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
class CommunityEmailServiceTest {

    @Mock
    private DataRepository dataRepository;

    private CommunityEmailService communityEmailService;

    @BeforeEach
    void setUp() {
        communityEmailService = new CommunityEmailService(dataRepository);
    }

    @Test
    void getEmailsByCityShouldReturnEmailsForMatchingCity() {

        Person john = createPerson(
                "John",
                "Culver",
                "john@email.com"
        );

        Person anna = createPerson(
                "Anna",
                "Culver",
                "anna@email.com"
        );

        when(dataRepository.getPersons())
                .thenReturn(List.of(john, anna));

        List<String> result =
                communityEmailService.getEmailsByCity("Culver");

        assertEquals(2, result.size());
        assertTrue(result.contains("john@email.com"));
        assertTrue(result.contains("anna@email.com"));
    }

    @Test
    void getEmailsByCityShouldReturnEmptyListForUnknownCity() {

        Person john = createPerson(
                "John",
                "Culver",
                "john@email.com"
        );

        when(dataRepository.getPersons())
                .thenReturn(List.of(john));

        List<String> result =
                communityEmailService.getEmailsByCity("Unknown");

        assertTrue(result.isEmpty());
    }

    @Test
    void getEmailsByCityShouldIgnoreCase() {

        Person john = createPerson(
                "John",
                "Culver",
                "john@email.com"
        );

        when(dataRepository.getPersons())
                .thenReturn(List.of(john));

        List<String> result =
                communityEmailService.getEmailsByCity("culver");

        assertEquals(
                List.of("john@email.com"),
                result
        );
    }

    @Test
    void getEmailsByCityShouldRemoveDuplicateEmails() {

        Person john = createPerson(
                "John",
                "Culver",
                "family@email.com"
        );

        Person jane = createPerson(
                "Jane",
                "Culver",
                "family@email.com"
        );

        when(dataRepository.getPersons())
                .thenReturn(List.of(john, jane));

        List<String> result =
                communityEmailService.getEmailsByCity("Culver");

        assertEquals(1, result.size());
        assertEquals("family@email.com", result.getFirst());
    }

    private Person createPerson(
            String firstName,
            String city,
            String email
    ) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setCity(city);
        person.setEmail(email);

        return person;
    }
}