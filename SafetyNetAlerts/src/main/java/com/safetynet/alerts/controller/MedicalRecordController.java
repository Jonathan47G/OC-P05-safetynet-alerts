package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.MedicalRecord;
import com.safetynet.alerts.service.MedicalRecordService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    private static final Logger logger =
            LogManager.getLogger(MedicalRecordController.class);

    private final MedicalRecordService medicalRecordService;

    public MedicalRecordController(
            MedicalRecordService medicalRecordService
    ) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    public ResponseEntity<MedicalRecord> addMedicalRecord(
            @RequestBody MedicalRecord medicalRecord
    ) {

        logger.info(
                "POST /medicalRecord requested for: {} {}",
                medicalRecord.getFirstName(),
                medicalRecord.getLastName()
        );

        MedicalRecord created =
                medicalRecordService.addMedicalRecord(
                        medicalRecord
                );

        logger.info(
                "POST /medicalRecord successful for: {} {}",
                created.getFirstName(),
                created.getLastName()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping
    public ResponseEntity<?> updateMedicalRecord(
            @RequestBody MedicalRecord medicalRecord
    ) {

        logger.info(
                "PUT /medicalRecord requested for: {} {}",
                medicalRecord.getFirstName(),
                medicalRecord.getLastName()
        );

        Optional<MedicalRecord> updated =
                medicalRecordService.updateMedicalRecord(
                        medicalRecord
                );

        if (updated.isEmpty()) {

            logger.error(
                    "PUT /medicalRecord failed: {} {} not found",
                    medicalRecord.getFirstName(),
                    medicalRecord.getLastName()
            );

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of());
        }

        logger.info(
                "PUT /medicalRecord successful for: {} {}",
                medicalRecord.getFirstName(),
                medicalRecord.getLastName()
        );

        return ResponseEntity.ok(updated.get());
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMedicalRecord(
            @RequestParam String firstName,
            @RequestParam String lastName
    ) {

        logger.info(
                "DELETE /medicalRecord requested for: {} {}",
                firstName,
                lastName
        );

        boolean deleted =
                medicalRecordService.deleteMedicalRecord(
                        firstName,
                        lastName
                );

        if (!deleted) {

            logger.error(
                    "DELETE /medicalRecord failed: {} {} not found",
                    firstName,
                    lastName
            );

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of());
        }

        logger.info(
                "DELETE /medicalRecord successful for: {} {}",
                firstName,
                lastName
        );

        return ResponseEntity.ok(Map.of());
    }
}