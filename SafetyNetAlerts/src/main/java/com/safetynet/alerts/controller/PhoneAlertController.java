package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.PhoneAlertService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class PhoneAlertController {

    private static final Logger logger =
            LogManager.getLogger(PhoneAlertController.class);

    private final PhoneAlertService phoneAlertService;

    public PhoneAlertController(
            PhoneAlertService phoneAlertService
    ) {
        this.phoneAlertService = phoneAlertService;
    }

    @GetMapping("/phoneAlert")
    public ResponseEntity<?> getPhoneAlert(
            @RequestParam String firestation
    ) {

        logger.info(
                "GET /phoneAlert requested for firestation: {}",
                firestation
        );

        List<String> phones =
                phoneAlertService.getPhonesByFirestation(
                        firestation
                );

        if (phones.isEmpty()) {

            logger.info(
                    "GET /phoneAlert response: no data found for station {}",
                    firestation
            );

            return ResponseEntity.ok(Map.of());
        }

        logger.info(
                "GET /phoneAlert response: {}",
                phones
        );

        return ResponseEntity.ok(phones);
    }
}