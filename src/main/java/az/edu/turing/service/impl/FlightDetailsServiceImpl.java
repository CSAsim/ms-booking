package az.edu.turing.service.impl;

import az.edu.turing.domain.entity.FlightDetailsEntity;
import az.edu.turing.service.FlightDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightDetailsServiceImpl implements FlightDetailsService {
    @Override
    public List<FlightDetailsEntity> findAll() {
        return List.of();
    }

    @Override
    public Optional<FlightDetailsEntity> findByFlightId(Long flightId) {
        return Optional.empty();
    }

    @Override
    public Optional<FlightDetailsEntity> findByFlightNumber(String flightNumber) {
        return Optional.empty();
    }

    @Override
    public FlightDetailsEntity create(FlightDetailsEntity details) {
        return null;
    }

    @Override
    public FlightDetailsEntity update(Long id, FlightDetailsEntity details) {
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
