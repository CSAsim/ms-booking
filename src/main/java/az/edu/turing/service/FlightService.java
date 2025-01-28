package az.edu.turing.service;

import az.edu.turing.model.FlightDto;
import az.edu.turing.model.enums.StatusMessage;
import az.edu.turing.model.request.flight.CreateFlightRequest;
import az.edu.turing.model.request.flight.UpdateFlightRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightService {

    List<FlightDto> findAll(String departure, String destination, LocalDateTime departureTime,
                            LocalDateTime arrivalTime);

    List<FlightDto> findAllIn24Hours();

    FlightDto findById(Long id);

    List<FlightDto> findByFlightNumber(String flightNumber);

    FlightDto create(Long id, CreateFlightRequest flightRequest);

    FlightDto update(Long userId, Long id, UpdateFlightRequest flightRequest);

    FlightDto updateFlightNumber(Long userId, Long id, String flightNumber);

    FlightDto updateFlightStatus(Long userId, Long id, StatusMessage flightStatus);

    void delete(Long id);

}
