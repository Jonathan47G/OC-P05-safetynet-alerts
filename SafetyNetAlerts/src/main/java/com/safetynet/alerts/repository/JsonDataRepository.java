package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;
import com.safetynet.alerts.model.SafetyNetData;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Repository
public class JsonDataRepository implements DataRepository {

    private static final Logger logger =
            LogManager.getLogger(JsonDataRepository.class);

    private final JsonMapper jsonMapper;

    private SafetyNetData data;

    public JsonDataRepository(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @PostConstruct
    public void loadData() {
        try {
            ClassPathResource resource =
                    new ClassPathResource("data.json");

            data = jsonMapper.readValue(
                    resource.getInputStream(),
                    SafetyNetData.class
            );

            logger.info("SafetyNet data loaded successfully");

        } catch (IOException exception) {
            logger.error(
                    "Unable to load SafetyNet data",
                    exception
            );

            throw new IllegalStateException(
                    "Unable to load data.json",
                    exception
            );
        }
    }

    @Override
    public List<Person> getPersons() {
        return data.getPersons();
    }

    @Override
    public List<FireStation> getFirestations() {
        return data.getFirestations();
    }

    @Override
    public List<MedicalRecord> getMedicalRecords() {
        return data.getMedicalrecords();
    }

    @Override
    public Person addPerson(Person person) {
        data.getPersons().add(person);
        return person;
    }

    @Override
    public Optional<Person> updatePerson(Person updatedPerson) {

        return data.getPersons()
                .stream()
                .filter(person ->
                        person.getFirstName()
                                .equalsIgnoreCase(updatedPerson.getFirstName())
                                &&
                                person.getLastName()
                                        .equalsIgnoreCase(updatedPerson.getLastName())
                )
                .findFirst()
                .map(person -> {

                    person.setAddress(updatedPerson.getAddress());
                    person.setCity(updatedPerson.getCity());
                    person.setZip(updatedPerson.getZip());
                    person.setPhone(updatedPerson.getPhone());
                    person.setEmail(updatedPerson.getEmail());

                    return person;
                });
    }

    @Override
    public boolean deletePerson(
            String firstName,
            String lastName
    ) {

        return data.getPersons()
                .removeIf(person ->
                        person.getFirstName().equalsIgnoreCase(firstName)
                                &&
                                person.getLastName().equalsIgnoreCase(lastName)
                );
    }

    @Override
    public FireStation addFireStation(FireStation fireStation) {
        data.getFirestations().add(fireStation);
        return fireStation;
    }

    @Override
    public Optional<FireStation> updateFireStation(
            FireStation updatedFireStation
    ) {

        return data.getFirestations()
                .stream()
                .filter(fireStation ->
                        fireStation.getAddress()
                                .equalsIgnoreCase(
                                        updatedFireStation.getAddress()
                                )
                )
                .findFirst()
                .map(fireStation -> {

                    fireStation.setStation(
                            updatedFireStation.getStation()
                    );

                    return fireStation;
                });
    }

    @Override
    public boolean deleteFireStationByAddress(String address) {

        return data.getFirestations()
                .removeIf(fireStation ->
                        fireStation.getAddress()
                                .equalsIgnoreCase(address)
                );
    }

    @Override
    public boolean deleteFireStationsByStation(
            String stationNumber
    ) {

        return data.getFirestations()
                .removeIf(fireStation ->
                        fireStation.getStation()
                                .equals(stationNumber)
                );
    }
}