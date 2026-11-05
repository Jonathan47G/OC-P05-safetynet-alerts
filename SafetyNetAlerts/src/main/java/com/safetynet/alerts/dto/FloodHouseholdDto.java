package com.safetynet.alerts.dto;

import java.util.List;

public record FloodHouseholdDto(
        String address,
        List<FloodPersonDto> persons
) {
}