package com.safetynet.alerts.controller;

import com.safetynet.alerts.dto.PersonInfoDto;
import com.safetynet.alerts.service.PersonInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonInfoController {

    private static final Logger logger =
            LogManager.getLogger(PersonInfoController.class);

    private final PersonInfoService personInfoService;

    public PersonInfoController(
            PersonInfoService personInfoService
    ) {
        this.personInfoService = personInfoService;
    }

    @GetMapping("/personInfo")
    public List<PersonInfoDto> getPersonInfo(
            @RequestParam String lastName
    ) {

        logger.info(
                "GET /personInfo requested for lastName: {}",
                lastName
        );

        List<PersonInfoDto> response =
                personInfoService.getPersonsByLastName(lastName);

        logger.info(
                "GET /personInfo response: {}",
                response
        );

        return response;
    }
}