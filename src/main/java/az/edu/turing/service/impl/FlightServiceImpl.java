package az.edu.turing.service.impl;

import az.edu.turing.domain.entity.FlightEntity;
import az.edu.turing.domain.repository.FlightRepository;
import az.edu.turing.domain.repository.FlightSpecification;
import az.edu.turing.exception.AlreadyExistsException;
import az.edu.turing.exception.InvalidInputException;
import az.edu.turing.exception.NotFoundException;
import az.edu.turing.mapper.FlightMapper;
import az.edu.turing.model.FlightDto;
import az.edu.turing.model.enums.StatusMessage;
import az.edu.turing.model.request.flight.CreateFlightRequest;
import az.edu.turing.model.request.flight.UpdateFlightRequest;
import az.edu.turing.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private FlightEntity existingFlight;
    private final FlightMapper flightMapper;
    private final FlightRepository flightRepository;

    public Page<FlightDto> findAll(String departure, String destination, LocalDateTime departureTime,
                                   LocalDateTime arrivalTime, Pageable pageable) {
        Specification<FlightEntity> spec = FlightSpecification.byFilters(departure, destination, departureTime, arrivalTime);
        Page<FlightEntity> flights = flightRepository.findAll(spec, pageable);
        return flights.map(flightMapper::toDto);
    }

    @Override
    public Page<FlightDto> findAllIn24Hours(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twentyFourHoursLater = now.plusHours(24);
        Specification<FlightEntity> spec = FlightSpecification.byFilters(null, null, now, twentyFourHoursLater);
        Page<FlightEntity> flights = flightRepository.findAll(spec, pageable);
        return flights.map(flightMapper::toDto);
    }

    @Override
    public FlightDto findById(Long id) {
        FlightEntity flightEntity = getFlightEntity(id);
        return flightMapper.toDto(flightEntity);
    }

    public Page<FlightDto> findByFlightNumber(String flightNumber, Pageable pageable) {
        Page<FlightEntity> flights = flightRepository.findByFlightNumber(flightNumber, pageable);
        if (flights.isEmpty()) {
            throw new NotFoundException("Flight with number " + flightNumber + " not found");
        }
        return flights.map(flightMapper::toDto);
    }

    @Override
    public FlightDto create(Long id, CreateFlightRequest flightRequest) {
        if (flightRepository.existsByFlightNumber(flightRequest.getFlightNumber())) {
            throw new AlreadyExistsException("Flight with number " + flightRequest.getFlightNumber() +
                    " already exists");
        }
        FlightEntity flightEntity = flightMapper.toEntity(flightRequest);
        flightEntity.setCreatedBy(id);
        flightEntity = flightRepository.save(flightEntity);

        return flightMapper.toDto(flightEntity);
    }

    @Override
    public FlightDto update(Long userId, Long id, UpdateFlightRequest flightRequest) {
        if (flightRequest.getDeparture() == null || flightRequest.getDestination() == null || flightRequest.getDepartureTime() == null) {
            throw new InvalidInputException("Departure, destination, and departure time must be provided.");
        }
        existingFlight = getFlightEntity(id);
        existingFlight.setUpdatedBy(userId);
        existingFlight.setDestination(flightRequest.getDestination());
        existingFlight.setDeparture(flightRequest.getDeparture());
        existingFlight.setDepartureTime(flightRequest.getDepartureTime());

        flightRepository.save(existingFlight);

        return flightMapper.toDto(existingFlight);
    }

    @Override
    public FlightDto updateFlightNumber(Long userId, Long id, String flightNumber) {
        existingFlight = getFlightEntity(id);
        existingFlight.setUpdatedBy(userId);
        existingFlight.setFlightNumber(flightNumber);

        flightRepository.save(existingFlight);

        return flightMapper.toDto(existingFlight);
    }

    @Override
    public FlightDto updateFlightStatus(Long userId, Long id, StatusMessage flightStatus) {
        existingFlight = getFlightEntity(id);
        existingFlight.setUpdatedBy(userId);
        existingFlight.setFlightStatus(flightStatus);

        flightRepository.save(existingFlight);

        return flightMapper.toDto(existingFlight);
    }

    public void decrementAvailableSeats(Long flightId) {
        existingFlight = getFlightEntity(flightId);
        flightRepository.decrementAvailableSeats(flightId);
    }

    @Override
    public void delete(Long id) {
        existingFlight = getFlightEntity(id);

        existingFlight.setFlightStatus(StatusMessage.CANCELLED);

        flightRepository.save(existingFlight);
    }

    private boolean existsByFlightNumber(String flightNumber) {
        return flightRepository.existsByFlightNumber(flightNumber);
    }

    private boolean existsById(Long id) {
        return flightRepository.existsById(id);
    }

    private FlightEntity getFlightEntity(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Flight with id " + id + " not found."));
    }
}
