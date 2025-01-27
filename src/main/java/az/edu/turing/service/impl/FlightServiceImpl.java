package az.edu.turing.service.impl;

import az.edu.turing.model.FlightDto;
import az.edu.turing.model.enums.StatusMessage;
import az.edu.turing.model.request.flight.CreateFlightRequest;
import az.edu.turing.model.request.flight.UpdateFlightRequest;
import az.edu.turing.service.FlightService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightServiceImpl implements FlightService {

    @Override
    public List<FlightDto> findAll() {
        return List.of();
    }

    @Override
    public List<FlightDto> findAllInLast24Hours() {
        return List.of();
    }

    @Override
    public Optional<FlightDto> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<FlightDto> findByFlightNumber(String flightNumber) {
        return Optional.empty();
    }

    @Override
    public FlightDto create(CreateFlightRequest flightRequest) {
        return null;
    }

    @Override
    public FlightDto update(Long id, UpdateFlightRequest flightRequest) {
        return null;
    }

    @Override
    public FlightDto updateFlightNumber(Long id, String flightNumber) {
        return null;
    }

    @Override
    public FlightDto updateFlightStatus(Long id, StatusMessage flightStatus) {
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
