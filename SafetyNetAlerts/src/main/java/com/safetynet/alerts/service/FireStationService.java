package com.safetynet.alerts.service;

import com.safetynet.alerts.model.FireStation;
import com.safetynet.alerts.repository.DataRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FireStationService {

    private static final Logger logger =
            LogManager.getLogger(FireStationService.class);

    private final DataRepository dataRepository;

    public FireStationService(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public FireStation addFireStation(
            FireStation fireStation
    ) {

        logger.debug(
                "Adding firestation mapping: address={}, station={}",
                fireStation.getAddress(),
                fireStation.getStation()
        );

        return dataRepository.addFireStation(fireStation);
    }

    public Optional<FireStation> updateFireStation(
            FireStation fireStation
    ) {

        logger.debug(
                "Updating firestation mapping for address: {}",
                fireStation.getAddress()
        );

        return dataRepository.updateFireStation(fireStation);
    }

    public boolean deleteByAddress(String address) {

        logger.debug(
                "Deleting firestation mapping for address: {}",
                address
        );

        return dataRepository
                .deleteFireStationByAddress(address);
    }

    public boolean deleteByStation(
            String stationNumber
    ) {

        logger.debug(
                "Deleting mappings for firestation: {}",
                stationNumber
        );

        return dataRepository
                .deleteFireStationsByStation(stationNumber);
    }
}