package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.FloodHouseholdDto;
import com.safetynet.alerts.service.FloodService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FloodController {

    private static final Logger logger =
            LogManager.getLogger(FloodController.class);

    private final FloodService floodService;

    public FloodController(FloodService floodService) {
        this.floodService = floodService;
    }

    @GetMapping("/flood/stations")
    public List<FloodHouseholdDto> getFloodStations(
            @RequestParam List<String> stations
    ) {

        logger.info(
                "GET /flood/stations requested for stations: {}",
                stations
        );

        List<FloodHouseholdDto> response =
                floodService.getHouseholdsByStations(stations);

        logger.info(
                "GET /flood/stations response: {}",
                response
        );

        return response;
    }
}