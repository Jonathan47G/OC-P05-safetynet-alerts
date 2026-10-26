package com.safetynet.alerts.repository;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.model.Person;

import java.util.List;

public interface DataRepository {

    List<Person> getPersons();

    List<FireStation> getFirestations();

    List<MedicalRecord> getMedicalRecords();
}