package com.safetynet.alerts.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AgeCalculatorTest {

    private final AgeCalculator ageCalculator =
            new AgeCalculator();

    @Test
    void calculateAgeShouldReturnCorrectAge() {

        LocalDate birthDate =
                LocalDate.now().minusYears(18);

        String birthdate = birthDate.format(
                DateTimeFormatter.ofPattern("MM/dd/yyyy")
        );

        assertEquals(
                18,
                ageCalculator.calculateAge(birthdate)
        );
    }
}