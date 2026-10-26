package com.safetynet.alerts.service;

import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.repository.DataRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunityEmailService {

    private static final Logger logger =
            LogManager.getLogger(CommunityEmailService.class);

    private final DataRepository dataRepository;

    public CommunityEmailService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public List<String> getEmailsByCity(String city) {

        logger.debug("Searching emails for city: {}", city);

        return dataRepository.getPersons()
                .stream()
                .filter(person -> person.getCity().equalsIgnoreCase(city))
                .map(Person::getEmail)
                .distinct()
                .toList();
    }
}