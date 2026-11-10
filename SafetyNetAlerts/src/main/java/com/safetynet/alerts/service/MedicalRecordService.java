package com.safetynet.alerts.service;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.repository.DataRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MedicalRecordService {

    private static final Logger logger =
            LogManager.getLogger(MedicalRecordService.class);

    private final DataRepository dataRepository;

    public MedicalRecordService(
            DataRepository dataRepository
    ) {
        this.dataRepository = dataRepository;
    }

    public MedicalRecord addMedicalRecord(
            MedicalRecord medicalRecord
    ) {

        logger.debug(
                "Adding medical record for: {} {}",
                medicalRecord.getFirstName(),
                medicalRecord.getLastName()
        );

        return dataRepository.addMedicalRecord(
                medicalRecord
        );
    }

    public Optional<MedicalRecord> updateMedicalRecord(
            MedicalRecord medicalRecord
    ) {

        logger.debug(
                "Updating medical record for: {} {}",
                medicalRecord.getFirstName(),
                medicalRecord.getLastName()
        );

        return dataRepository.updateMedicalRecord(
                medicalRecord
        );
    }

    public boolean deleteMedicalRecord(
            String firstName,
            String lastName
    ) {

        logger.debug(
                "Deleting medical record for: {} {}",
                firstName,
                lastName
        );

        return dataRepository.deleteMedicalRecord(
                firstName,
                lastName
        );
    }
}