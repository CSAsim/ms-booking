package az.edu.turing.service;

import az.edu.turing.domain.entity.FlightEntity;
import az.edu.turing.model.enums.StatusMessage;

import java.util.List;
import java.util.Optional;

public interface FlightService {

    List<FlightEntity> findAll();
    List<FlightEntity> findAllInLast24Hours();
    Optional<FlightEntity> findById(Long id);
    Optional<FlightEntity> findByFlightNumber(String flightNumber);
    FlightEntity create(FlightEntity flightEntity);
    FlightEntity update(Long id, FlightEntity flightEntity);
    FlightEntity updateFlightNumber(Long id, String flightNumber);
    FlightEntity updateFlightStatus(Long id, StatusMessage flightStatus);
    void delete(Long id); // Soft delete: Set flightStatus to DELETED.
    boolean existsByFlightNumber(String flightNumber);
    boolean existsById(Long id);
}
