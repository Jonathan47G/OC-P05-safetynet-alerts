package com.safetynet.alerts.dto;

import java.util.List;

public record FirestationResponseDto(
        List<FirestationPersonDto> persons,
        int adultCount,
        int childCount
) {
}