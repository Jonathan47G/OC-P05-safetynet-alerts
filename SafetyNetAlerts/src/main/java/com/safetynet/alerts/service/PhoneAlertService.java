package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PhoneAlertService {

    private static final Logger logger =
            LogManager.getLogger(PhoneAlertService.class);

    private final DataRepository dataRepository;

    public PhoneAlertService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public List<String> getPhonesByFirestation(String stationNumber) {

        logger.debug(
                "Searching phone numbers for firestation: {}",
                stationNumber
        );

        Set<String> addresses = dataRepository.getFirestations()
                .stream()
                .filter(fireStation ->
                        fireStation.getStation()
                                .equals(stationNumber))
                .map(FireStation::getAddress)
                .collect(Collectors.toSet());

        return dataRepository.getPersons()
                .stream()
                .filter(person ->
                        addresses.contains(person.getAddress()))
                .map(Person::getPhone)
                .distinct()
                .toList();
    }
}