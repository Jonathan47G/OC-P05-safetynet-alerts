package com.safetynet.alerts.controller;

import com.safetynet.alerts.service.CommunityEmailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CommunityEmailController {

    private static final Logger logger =
            LogManager.getLogger(CommunityEmailController.class);

    private final CommunityEmailService communityEmailService;

    public CommunityEmailController(CommunityEmailService communityEmailService) {
        this.communityEmailService = communityEmailService;
    }

    @GetMapping("/communityEmail")
    public List<String> getCommunityEmails(@RequestParam String city) {

        logger.info("GET /communityEmail requested for city: {}", city);

        List<String> emails = communityEmailService.getEmailsByCity(city);

        logger.info("GET /communityEmail response: {}", emails);

        return emails;
    }
}