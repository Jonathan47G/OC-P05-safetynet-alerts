package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FloodHouseholdDto;
import com.safetynet.alerts.dto.FloodPersonDto;
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
public class FloodService {

    private static final Logger logger =
            LogManager.getLogger(FloodService.class);

    private final DataRepository dataRepository;
    private final AgeCalculator ageCalculator;

    public FloodService(
            DataRepository dataRepository,
            AgeCalculator ageCalculator
    ) {
        this.dataRepository = dataRepository;
        this.ageCalculator = ageCalculator;
    }

    public List<FloodHouseholdDto> getHouseholdsByStations(
            List<String> stationNumbers
    ) {

        logger.debug(
                "Searching households covered by stations: {}",
                stationNumbers
        );

        Set<String> addresses =
                dataRepository.getFirestations()
                        .stream()
                        .filter(fireStation ->
                                stationNumbers.contains(
                                        fireStation.getStation()
                                ))
                        .map(FireStation::getAddress)
                        .collect(Collectors.toSet());

        return addresses.stream()
                .map(this::createHousehold)
                .filter(household ->
                        !household.persons().isEmpty())
                .toList();
    }

    private FloodHouseholdDto createHousehold(String address) {

        List<FloodPersonDto> persons =
                dataRepository.getPersons()
                        .stream()
                        .filter(person ->
                                person.getAddress()
                                        .equalsIgnoreCase(address))
                        .map(this::createFloodPerson)
                        .flatMap(Optional::stream)
                        .toList();

        return new FloodHouseholdDto(
                address,
                persons
        );
    }

    private Optional<FloodPersonDto> createFloodPerson(
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
                .findFirst()
                .map(record ->
                        createFloodPersonDto(person, record)
                );
    }

    private FloodPersonDto createFloodPersonDto(
            Person person,
            MedicalRecord record
    ) {

        return new FloodPersonDto(
                person.getFirstName(),
                person.getLastName(),
                person.getPhone(),
                ageCalculator.calculateAge(
                        record.getBirthdate()
                ),
                record.getMedications(),
                record.getAllergies()
        );
    }
}