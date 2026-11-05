package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FireResponseDto;
import com.safetynet.alerts.service.FireService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
public class FireController {

    private static final Logger logger =
            LogManager.getLogger(FireController.class);

    private final FireService fireService;

    public FireController(FireService fireService) {
        this.fireService = fireService;
    }

    @GetMapping("/fire")
    public ResponseEntity<?> getFire(
            @RequestParam String address
    ) {

        logger.info(
                "GET /fire requested for address: {}",
                address
        );

        Optional<FireResponseDto> response =
                fireService.getPersonsByAddress(address);

        if (response.isEmpty()) {

            logger.info(
                    "GET /fire response: no data found for address {}",
                    address
            );

            return ResponseEntity.ok(Map.of());
        }

        logger.info(
                "GET /fire response: {}",
                response.get()
        );

        return ResponseEntity.ok(response.get());
    }
}