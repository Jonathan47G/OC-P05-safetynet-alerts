package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.service.PersonService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/person")
public class PersonController {

    private static final Logger logger =
            LogManager.getLogger(PersonController.class);

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<Person> addPerson(
            @RequestBody Person person
    ) {

        logger.info(
                "POST /person requested for: {} {}",
                person.getFirstName(),
                person.getLastName()
        );

        Person createdPerson =
                personService.addPerson(person);

        logger.info(
                "POST /person response: {} {} created",
                createdPerson.getFirstName(),
                createdPerson.getLastName()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdPerson);
    }

    @PutMapping
    public ResponseEntity<?> updatePerson(
            @RequestBody Person person
    ) {

        logger.info(
                "PUT /person requested for: {} {}",
                person.getFirstName(),
                person.getLastName()
        );

        Optional<Person> updatedPerson =
                personService.updatePerson(person);

        if (updatedPerson.isEmpty()) {

            logger.error(
                    "PUT /person failed: person {} {} not found",
                    person.getFirstName(),
                    person.getLastName()
            );

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of());
        }

        logger.info(
                "PUT /person successful for: {} {}",
                person.getFirstName(),
                person.getLastName()
        );

        return ResponseEntity.ok(updatedPerson.get());
    }

    @DeleteMapping
    public ResponseEntity<?> deletePerson(
            @RequestParam String firstName,
            @RequestParam String lastName
    ) {

        logger.info(
                "DELETE /person requested for: {} {}",
                firstName,
                lastName
        );

        boolean deleted =
                personService.deletePerson(
                        firstName,
                        lastName
                );

        if (!deleted) {

            logger.error(
                    "DELETE /person failed: person {} {} not found",
                    firstName,
                    lastName
            );

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of());
        }

        logger.info(
                "DELETE /person successful for: {} {}",
                firstName,
                lastName
        );

        return ResponseEntity.ok(Map.of());
    }
}