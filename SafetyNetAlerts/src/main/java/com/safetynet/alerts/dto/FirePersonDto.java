package com.safetynet.alerts.dto;

import java.util.List;

public record FirePersonDto(
        String firstName,
        String lastName,
        String phone,
        int age,
        List<String> medications,
        List<String> allergies
) {
}