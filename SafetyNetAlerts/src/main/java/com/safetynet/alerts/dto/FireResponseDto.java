package com.safetynet.alerts.dto;

import java.util.List;

public record FireResponseDto(
        String stationNumber,
        List<FirePersonDto> persons
) {
}