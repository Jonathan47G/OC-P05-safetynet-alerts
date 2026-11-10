package com.safetynet.alerts.controller;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.service.FireStationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/firestation")
public class FireStationController {

    private static final Logger logger =
            LogManager.getLogger(FireStationController.class);

    private final FireStationService fireStationService;

    public FireStationController(
            FireStationService fireStationService
    ) {
        this.fireStationService = fireStationService;
    }

    @PostMapping
    public ResponseEntity<FireStation> addFireStation(
            @RequestBody FireStation fireStation
    ) {

        logger.info(
                "POST /firestation requested: address={}, station={}",
                fireStation.getAddress(),
                fireStation.getStation()
        );

        FireStation created =
                fireStationService.addFireStation(fireStation);

        logger.info(
                "POST /firestation successful: {}",
                created.getAddress()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping
    public ResponseEntity<?> updateFireStation(
            @RequestBody FireStation fireStation
    ) {

        logger.info(
                "PUT /firestation requested for address: {}",
                fireStation.getAddress()
        );

        Optional<FireStation> updated =
                fireStationService.updateFireStation(
                        fireStation
                );

        if (updated.isEmpty()) {

            logger.error(
                    "PUT /firestation failed: address not found: {}",
                    fireStation.getAddress()
            );

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of());
        }

        logger.info(
                "PUT /firestation successful for address: {}",
                fireStation.getAddress()
        );

        return ResponseEntity.ok(updated.get());
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFireStation(
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String stationNumber
    ) {

        boolean deleted;

        if (address != null) {

            logger.info(
                    "DELETE /firestation requested for address: {}",
                    address
            );

            deleted =
                    fireStationService.deleteByAddress(address);

        } else if (stationNumber != null) {

            logger.info(
                    "DELETE /firestation requested for station: {}",
                    stationNumber
            );

            deleted =
                    fireStationService.deleteByStation(
                            stationNumber
                    );

        } else {

            logger.error(
                    "DELETE /firestation failed: no parameter provided"
            );

            return ResponseEntity
                    .badRequest()
                    .body(Map.of());
        }

        if (!deleted) {

            logger.error(
                    "DELETE /firestation failed: mapping not found"
            );

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of());
        }

        logger.info(
                "DELETE /firestation successful"
        );

        return ResponseEntity.ok(Map.of());
    }
}