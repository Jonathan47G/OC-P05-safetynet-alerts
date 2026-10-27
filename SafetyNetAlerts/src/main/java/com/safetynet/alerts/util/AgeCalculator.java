package com.safetynet.alerts.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Component
public class AgeCalculator {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public int calculateAge(String birthdate) {

        LocalDate birthDate = LocalDate.parse(
                birthdate,
                DATE_FORMAT
        );

        return Period.between(
                birthDate,
                LocalDate.now()
        ).getYears();
    }
}