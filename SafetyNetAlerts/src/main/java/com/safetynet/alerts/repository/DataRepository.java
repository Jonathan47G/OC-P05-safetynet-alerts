package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.*;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.List;

@Repository
public class DataRepository {

    private static final Logger logger = LogManager.getLogger(DataRepository.class);

    private final JsonMapper jsonMapper;

    private SafetyNetData data;

    public DataRepository(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @PostConstruct
    public void loadData() {
        try {
            ClassPathResource resource = new ClassPathResource("data.json");

            data = jsonMapper.readValue(
                    resource.getInputStream(),
                    SafetyNetData.class
            );

            logger.info("SafetyNet data loaded successfully");

        } catch (IOException exception) {
            logger.error("Unable to load SafetyNet data", exception);
            throw new IllegalStateException(
                    "Unable to load data.json",
                    exception
            );
        }
    }

    public List<Person> getPersons() {
        return data.getPersons();
    }

    public List<FireStation> getFirestations() {
        return data.getFirestations();
    }

    public List<MedicalRecord> getMedicalRecords() {
        return data.getMedicalrecords();
    }
}