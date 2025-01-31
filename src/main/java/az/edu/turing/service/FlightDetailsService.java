package az.edu.turing.service;

import az.edu.turing.model.FlightDetailsDto;
import az.edu.turing.model.request.flightDetails.CreateFlightDetailsRequest;
import az.edu.turing.model.request.flightDetails.UpdateFlightDetailsRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FlightDetailsService {

    Page<FlightDetailsDto> findAll(Pageable pageable);

    FlightDetailsDto findByFlightId(Long flightId); // flightId from FlightEntity.

    FlightDetailsDto create(Long userId, CreateFlightDetailsRequest request);

    FlightDetailsDto updateByFlightId(Long userId, Long id, UpdateFlightDetailsRequest request);

    void deleteByFlightId(Long userId, Long flightId); // Delete flight details for the flight ID.

}
