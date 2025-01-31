package az.edu.turing.service;

import az.edu.turing.model.FlightDto;
import az.edu.turing.model.enums.StatusMessage;
import az.edu.turing.model.request.flight.CreateFlightRequest;
import az.edu.turing.model.request.flight.UpdateFlightRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface FlightService {

    Page<FlightDto> findAll(String departure, String destination, LocalDateTime departureTime,
                            LocalDateTime arrivalTime, Pageable pageable);

    Page<FlightDto> findAllIn24Hours(Pageable pageable);

    FlightDto findById(Long id);

    FlightDto findByFlightNumber(String flightNumber);

    FlightDto create(Long id, CreateFlightRequest flightRequest);

    FlightDto update(Long userId, Long id, UpdateFlightRequest flightRequest);

    FlightDto updateFlightNumber(Long userId, Long id, String flightNumber);

    FlightDto updateFlightStatus(Long userId, Long id, StatusMessage flightStatus);

    void delete(Long userId, Long id);

}
