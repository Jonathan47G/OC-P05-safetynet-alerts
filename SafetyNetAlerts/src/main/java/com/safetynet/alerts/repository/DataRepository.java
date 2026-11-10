package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

import java.util.List;
import java.util.Optional;

public interface DataRepository {

    List<Person> getPersons();

    List<FireStation> getFirestations();

    List<MedicalRecord> getMedicalRecords();

    Person addPerson(Person person);

    Optional<Person> updatePerson(Person person);

    boolean deletePerson(String firstName, String lastName);

    FireStation addFireStation(FireStation fireStation);

    Optional<FireStation> updateFireStation(FireStation fireStation);

    boolean deleteFireStationByAddress(String address);

    boolean deleteFireStationsByStation(String stationNumber);
}