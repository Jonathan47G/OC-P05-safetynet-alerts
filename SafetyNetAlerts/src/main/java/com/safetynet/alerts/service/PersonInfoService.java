package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.PersonInfoDto;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import com.safetynet.alerts.util.AgeCalculator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonInfoService {

    private static final Logger logger =
            LogManager.getLogger(PersonInfoService.class);

    private final DataRepository dataRepository;
    private final AgeCalculator ageCalculator;

    public PersonInfoService(
            DataRepository dataRepository,
            AgeCalculator ageCalculator
    ) {
        this.dataRepository = dataRepository;
        this.ageCalculator = ageCalculator;
    }

    public List<PersonInfoDto> getPersonsByLastName(String lastName) {

        logger.debug(
                "Searching person information for last name: {}",
                lastName
        );

        return dataRepository.getPersons()
                .stream()
                .filter(person ->
                        person.getLastName()
                                .equalsIgnoreCase(lastName))
                .map(this::createPersonInfo)
                .flatMap(Optional::stream)
                .toList();
    }

    private Optional<PersonInfoDto> createPersonInfo(Person person) {

        return dataRepository.getMedicalRecords()
                .stream()
                .filter(record ->
                        record.getFirstName()
                                .equalsIgnoreCase(person.getFirstName())
                                &&
                                record.getLastName()
                                        .equalsIgnoreCase(person.getLastName()))
                .findFirst()
                .map(record -> new PersonInfoDto(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        ageCalculator.calculateAge(
                                record.getBirthdate()
                        ),
                        person.getEmail(),
                        record.getMedications(),
                        record.getAllergies()
                ));
    }
}