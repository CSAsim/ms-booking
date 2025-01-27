package az.edu.turing.service;

import az.edu.turing.model.FlightDetailsDto;
import az.edu.turing.model.request.flightDetails.CreateFlightDetailsRequest;
import az.edu.turing.model.request.flightDetails.UpdateFlightDetailsRequest;

import java.util.List;
import java.util.Optional;

public interface FlightDetailsService {

    List<FlightDetailsDto> findAll();
    Optional<FlightDetailsDto> findByFlightId(Long flightId); // flightId from FlightEntity.
    Optional<FlightDetailsDto> findByFlightNumber(String flightNumber); // flightNumber from FlightEntity.
    FlightDetailsDto create(CreateFlightDetailsRequest flightDetailsRequest);
    FlightDetailsDto update(Long id, UpdateFlightDetailsRequest flightDetailsRequest);
    void delete(Long id); // Hard delete.
    void deleteByFlightId(Long flightId); // Delete flight details for the flight ID.
    boolean existsById(Long id);
}
