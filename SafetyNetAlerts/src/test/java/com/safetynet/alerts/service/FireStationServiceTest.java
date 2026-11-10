package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.DataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FireStationServiceTest {

    @Mock
    private DataRepository dataRepository;

    private FireStationService fireStationService;

    @BeforeEach
    void setUp() {
        fireStationService = new FireStationService(dataRepository);
    }

    @Test
    void addFireStationShouldReturnAddedMapping() {

        FireStation fireStation = createFireStation(
                "1 Test St",
                "5"
        );

        when(dataRepository.addFireStation(fireStation))
                .thenReturn(fireStation);

        FireStation result =
                fireStationService.addFireStation(fireStation);

        assertEquals(fireStation, result);

        verify(dataRepository)
                .addFireStation(fireStation);
    }

    @Test
    void updateFireStationShouldReturnUpdatedMapping() {

        FireStation fireStation = createFireStation(
                "1 Test St",
                "2"
        );

        when(dataRepository.updateFireStation(fireStation))
                .thenReturn(Optional.of(fireStation));

        Optional<FireStation> result =
                fireStationService.updateFireStation(fireStation);

        assertTrue(result.isPresent());
        assertEquals("2", result.get().getStation());
    }

    @Test
    void updateFireStationShouldReturnEmptyWhenAddressDoesNotExist() {

        FireStation fireStation = createFireStation(
                "Unknown",
                "2"
        );

        when(dataRepository.updateFireStation(fireStation))
                .thenReturn(Optional.empty());

        Optional<FireStation> result =
                fireStationService.updateFireStation(fireStation);

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteByAddressShouldReturnTrueWhenMappingExists() {

        when(dataRepository.deleteFireStationByAddress("1 Test St"))
                .thenReturn(true);

        assertTrue(
                fireStationService.deleteByAddress("1 Test St")
        );
    }

    @Test
    void deleteByAddressShouldReturnFalseWhenMappingDoesNotExist() {

        when(dataRepository.deleteFireStationByAddress("Unknown"))
                .thenReturn(false);

        assertFalse(
                fireStationService.deleteByAddress("Unknown")
        );
    }

    @Test
    void deleteByStationShouldReturnTrueWhenMappingExists() {

        when(dataRepository.deleteFireStationsByStation("3"))
                .thenReturn(true);

        assertTrue(
                fireStationService.deleteByStation("3")
        );
    }

    @Test
    void deleteByStationShouldReturnFalseWhenMappingDoesNotExist() {

        when(dataRepository.deleteFireStationsByStation("999"))
                .thenReturn(false);

        assertFalse(
                fireStationService.deleteByStation("999")
        );
    }

    private FireStation createFireStation(
            String address,
            String station
    ) {
        FireStation fireStation = new FireStation();
        fireStation.setAddress(address);
        fireStation.setStation(station);
        return fireStation;
    }
}