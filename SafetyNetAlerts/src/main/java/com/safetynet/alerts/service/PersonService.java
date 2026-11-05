package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonService {

    private static final Logger logger =
            LogManager.getLogger(PersonService.class);

    private final DataRepository dataRepository;

    public PersonService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public Person addPerson(Person person) {

        logger.debug(
                "Adding person: {} {}",
                person.getFirstName(),
                person.getLastName()
        );

        return dataRepository.addPerson(person);
    }

    public Optional<Person> updatePerson(Person person) {

        logger.debug(
                "Updating person: {} {}",
                person.getFirstName(),
                person.getLastName()
        );

        return dataRepository.updatePerson(person);
    }

    public boolean deletePerson(
            String firstName,
            String lastName
    ) {

        logger.debug(
                "Deleting person: {} {}",
                firstName,
                lastName
        );

        return dataRepository.deletePerson(
                firstName,
                lastName
        );
    }
}