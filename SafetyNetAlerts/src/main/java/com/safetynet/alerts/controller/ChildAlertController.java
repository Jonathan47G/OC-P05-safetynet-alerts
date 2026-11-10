package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.ChildAlertDto;
import com.safetynet.alerts.service.ChildAlertService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ChildAlertController {

    private static final Logger logger =
            LogManager.getLogger(ChildAlertController.class);

    private final ChildAlertService childAlertService;

    public ChildAlertController(
            ChildAlertService childAlertService
    ) {
        this.childAlertService = childAlertService;
    }

    @GetMapping("/childAlert")
    public ResponseEntity<?> getChildAlert(
            @RequestParam String address
    ) {

        logger.info(
                "GET /childAlert requested for address: {}",
                address
        );

        if (!childAlertService.addressExists(address)) {

            logger.info(
                    "GET /childAlert response: address not found: {}",
                    address
            );

            return ResponseEntity.ok(Map.of());
        }

        List<ChildAlertDto> response =
                childAlertService.getChildrenByAddress(address);

        logger.info(
                "GET /childAlert response: {}",
                response
        );

        return ResponseEntity.ok(response);
    }
}