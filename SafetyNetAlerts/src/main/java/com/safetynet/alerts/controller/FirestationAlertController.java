package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FirestationResponseDto;
import com.safetynet.alerts.service.FirestationAlertService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirestationAlertController {

    private static final Logger logger =
            LogManager.getLogger(FirestationAlertController.class);

    private final FirestationAlertService firestationAlertService;

    public FirestationAlertController(
            FirestationAlertService firestationAlertService
    ) {
        this.firestationAlertService = firestationAlertService;
    }

    @GetMapping("/firestation")
    public FirestationResponseDto getFirestation(
            @RequestParam String stationNumber
    ) {

        logger.info(
                "GET /firestation requested for stationNumber: {}",
                stationNumber
        );

        FirestationResponseDto response =
                firestationAlertService.getPersonsByStation(
                        stationNumber
                );

        logger.info(
                "GET /firestation response: {}",
                response
        );

        return response;
    }
}