package az.edu.turing.service;

import az.edu.turing.domain.entity.FlightDetailsEntity;

import java.util.List;
import java.util.Optional;

public interface FlightDetailsService {

    List<FlightDetailsEntity> findAll();
    Optional<FlightDetailsEntity> findByFlightId(Long flightId); // flightId from FlightEntity.
    Optional<FlightDetailsEntity> findByFlightNumber(String flightNumber); // flightNumber from FlightEntity.
    FlightDetailsEntity create(FlightDetailsEntity details);
    FlightDetailsEntity update(Long id, FlightDetailsEntity details);
    void delete(Long id); // Hard delete.
    void deleteByFlightId(Long flightId); // Delete flight details for the flight ID.
    boolean existsById(Long id);
}
