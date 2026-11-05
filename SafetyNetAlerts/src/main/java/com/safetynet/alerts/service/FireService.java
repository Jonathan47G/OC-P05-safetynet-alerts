package com.safetynet.alerts.service;

import com.safetynet.alerts.dto.FirePersonDto;
import com.safetynet.alerts.dto.FireResponseDto;
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

@Service
public class FireService {

    private static final Logger logger =
            LogManager.getLogger(FireService.class);

    private final DataRepository dataRepository;
    private final AgeCalculator ageCalculator;

    public FireService(
            DataRepository dataRepository,
            AgeCalculator ageCalculator
    ) {
        this.dataRepository = dataRepository;
        this.ageCalculator = ageCalculator;
    }

    public Optional<FireResponseDto> getPersonsByAddress(String address) {

        logger.debug(
                "Searching fire information for address: {}",
                address
        );

        Optional<FireStation> fireStation =
                dataRepository.getFirestations()
                        .stream()
                        .filter(station ->
                                station.getAddress()
                                        .equalsIgnoreCase(address))
                        .findFirst();

        if (fireStation.isEmpty()) {
            logger.debug(
                    "No firestation found for address: {}",
                    address
            );

            return Optional.empty();
        }

        List<FirePersonDto> persons =
                dataRepository.getPersons()
                        .stream()
                        .filter(person ->
                                person.getAddress()
                                        .equalsIgnoreCase(address))
                        .map(this::createFirePerson)
                        .flatMap(Optional::stream)
                        .toList();

        return Optional.of(
                new FireResponseDto(
                        fireStation.get().getStation(),
                        persons
                )
        );
    }

    private Optional<FirePersonDto> createFirePerson(Person person) {

        Optional<MedicalRecord> medicalRecord =
                dataRepository.getMedicalRecords()
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

        return medicalRecord.map(record ->
                new FirePersonDto(
                        person.getFirstName(),
                        person.getLastName(),
                        person.getPhone(),
                        ageCalculator.calculateAge(
                                record.getBirthdate()
                        ),
                        record.getMedications(),
                        record.getAllergies()
                )
        );
    }
}