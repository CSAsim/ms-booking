package az.edu.turing.service;

import az.edu.turing.model.FlightDto;
import az.edu.turing.model.enums.StatusMessage;
import az.edu.turing.model.request.flight.CreateFlightRequest;
import az.edu.turing.model.request.flight.UpdateFlightRequest;

import java.util.List;
import java.util.Optional;

public interface FlightService {

    List<FlightDto> findAll();
    List<FlightDto> findAllInLast24Hours();
    Optional<FlightDto> findById(Long id);
    Optional<FlightDto> findByFlightNumber(String flightNumber);
    FlightDto create(CreateFlightRequest flightRequest);
    FlightDto update(Long id, UpdateFlightRequest flightRequest);
    FlightDto updateFlightNumber(Long id, String flightNumber);
    FlightDto updateFlightStatus(Long id, StatusMessage flightStatus);
    void delete(Long id); // Soft delete: Set flightStatus to DELETED.
    boolean existsByFlightNumber(String flightNumber);
    boolean existsById(Long id);
}
