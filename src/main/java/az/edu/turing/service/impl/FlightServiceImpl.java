package az.edu.turing.service.impl;

import az.edu.turing.domain.entity.FlightEntity;
import az.edu.turing.enums.StatusMessage;
import az.edu.turing.service.FlightService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightServiceImpl implements FlightService {
    @Override
    public List<FlightEntity> findAll() {
        return List.of();
    }

    @Override
    public List<FlightEntity> findAllInLast24Hours() {
        return List.of();
    }

    @Override
    public Optional<FlightEntity> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<FlightEntity> findByFlightNumber(String flightNumber) {
        return Optional.empty();
    }

    @Override
    public FlightEntity create(FlightEntity flightEntity) {
        return null;
    }

    @Override
    public FlightEntity update(Long id, FlightEntity flightEntity) {
        return null;
    }

    @Override
    public FlightEntity updateFlightNumber(Long id, String flightNumber) {
        return null;
    }

    @Override
    public FlightEntity updateFlightStatus(Long id, StatusMessage flightStatus) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public boolean existsByFlightNumber(String flightNumber) {
        return false;
    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }
}
