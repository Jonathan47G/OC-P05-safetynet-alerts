package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.ChildAlertDto;
import com.safetynet.alerts.dto.HouseholdMemberDto;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import com.safetynet.alerts.util.AgeCalculator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChildAlertService {

    private static final Logger logger =
            LogManager.getLogger(ChildAlertService.class);

    private final DataRepository dataRepository;
    private final AgeCalculator ageCalculator;

    public ChildAlertService(
            DataRepository dataRepository,
            AgeCalculator ageCalculator
    ) {
        this.dataRepository = dataRepository;
        this.ageCalculator = ageCalculator;
    }

    public List<ChildAlertDto> getChildrenByAddress(String address) {

        logger.debug(
                "Searching children for address: {}",
                address
        );

        List<Person> household = dataRepository.getPersons()
                .stream()
                .filter(person ->
                        person.getAddress().equalsIgnoreCase(address))
                .toList();

        List<ChildAlertDto> children = new ArrayList<>();

        for (Person person : household) {

            Optional<MedicalRecord> medicalRecord =
                    findMedicalRecord(person);

            if (medicalRecord.isEmpty()) {
                continue;
            }

            int age = ageCalculator.calculateAge(
                    medicalRecord.get().getBirthdate()
            );

            if (age <= 18) {

                List<HouseholdMemberDto> householdMembers =
                        getOtherHouseholdMembers(
                                person,
                                household
                        );

                children.add(
                        new ChildAlertDto(
                                person.getFirstName(),
                                person.getLastName(),
                                age,
                                householdMembers
                        )
                );
            }
        }

        logger.debug(
                "{} child(ren) found for address: {}",
                children.size(),
                address
        );

        return children;
    }

    private Optional<MedicalRecord> findMedicalRecord(Person person) {

        return dataRepository.getMedicalRecords()
                .stream()
                .filter(record ->
                        record.getFirstName()
                                .equalsIgnoreCase(
                                        person.getFirstName()
                                )
                                &&
                                record.getLastName()
                                        .equalsIgnoreCase(
                                                person.getLastName()
                                        )
                )
                .findFirst();
    }

    private List<HouseholdMemberDto> getOtherHouseholdMembers(
            Person child,
            List<Person> household
    ) {

        return household.stream()
                .filter(person ->
                        !isSamePerson(person, child))
                .map(person ->
                        new HouseholdMemberDto(
                                person.getFirstName(),
                                person.getLastName()
                        )
                )
                .toList();
    }

    private boolean isSamePerson(
            Person first,
            Person second
    ) {

        return first.getFirstName()
                .equalsIgnoreCase(second.getFirstName())
                &&
                first.getLastName()
                        .equalsIgnoreCase(second.getLastName());
    }
}