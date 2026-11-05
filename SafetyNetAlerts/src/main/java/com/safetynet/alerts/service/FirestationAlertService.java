package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FirestationPersonDto;
import com.safetynet.alerts.dto.FirestationResponseDto;
import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import com.safetynet.alerts.util.AgeCalculator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FirestationAlertService {

    private static final Logger logger =
            LogManager.getLogger(FirestationAlertService.class);

    private final DataRepository dataRepository;
    private final AgeCalculator ageCalculator;

    public FirestationAlertService(
            DataRepository dataRepository,
            AgeCalculator ageCalculator
    ) {
        this.dataRepository = dataRepository;
        this.ageCalculator = ageCalculator;
    }

    public FirestationResponseDto getPersonsByStation(
            String stationNumber
    ) {

        logger.debug(
                "Searching persons covered by firestation: {}",
                stationNumber
        );

        Set<String> addresses = dataRepository.getFirestations()
                .stream()
                .filter(fireStation ->
                        fireStation.getStation()
                                .equals(stationNumber))
                .map(FireStation::getAddress)
                .collect(Collectors.toSet());

        List<Person> coveredPersons = dataRepository.getPersons()
                .stream()
                .filter(person ->
                        addresses.contains(person.getAddress()))
                .toList();

        List<FirestationPersonDto> persons = coveredPersons.stream()
                .map(person -> new FirestationPersonDto(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getAddress(),
                        person.getPhone()
                ))
                .toList();

        int childCount = 0;
        int adultCount = 0;

        for (Person person : coveredPersons) {

            Optional<MedicalRecord> medicalRecord =
                    findMedicalRecord(person);

            if (medicalRecord.isEmpty()) {
                continue;
            }

            int age = ageCalculator.calculateAge(
                    medicalRecord.get().getBirthdate()
            );

            if (age <= 18) {
                childCount++;
            } else {
                adultCount++;
            }
        }

        logger.debug(
                "Firestation {} covers {} adult(s) and {} child(ren)",
                stationNumber,
                adultCount,
                childCount
        );

        return new FirestationResponseDto(
                persons,
                adultCount,
                childCount
        );
    }

    private Optional<MedicalRecord> findMedicalRecord(
            Person person
    ) {
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
}