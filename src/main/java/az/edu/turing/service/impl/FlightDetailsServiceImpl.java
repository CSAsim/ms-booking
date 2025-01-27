package az.edu.turing.service.impl;

import az.edu.turing.model.FlightDetailsDto;
import az.edu.turing.model.request.flightDetails.CreateFlightDetailsRequest;
import az.edu.turing.model.request.flightDetails.UpdateFlightDetailsRequest;
import az.edu.turing.service.FlightDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightDetailsServiceImpl implements FlightDetailsService {

    @Override
    public List<FlightDetailsDto> findAll() {
        return List.of();
    }

    @Override
    public Optional<FlightDetailsDto> findByFlightId(Long flightId) {
        return Optional.empty();
    }

    @Override
    public Optional<FlightDetailsDto> findByFlightNumber(String flightNumber) {
        return Optional.empty();
    }

    @Override
    public FlightDetailsDto create(CreateFlightDetailsRequest flightDetailsRequest) {
        return null;
    }

    @Override
    public FlightDetailsDto update(Long id, UpdateFlightDetailsRequest flightDetailsRequest) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void deleteByFlightId(Long flightId) {

    }

    @Override
    public boolean existsById(Long id) {
        return false;
    }
}
