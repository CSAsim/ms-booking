package az.edu.turing.service;

import az.edu.turing.domain.entity.FlightEntity;
import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.domain.repository.FlightRepository;
import az.edu.turing.domain.repository.FlightSpecification;
import az.edu.turing.domain.repository.UserRepository;
import az.edu.turing.exception.AlreadyExistsException;
import az.edu.turing.exception.InvalidInputException;
import az.edu.turing.exception.NotFoundException;
import az.edu.turing.mapper.FlightMapper;
import az.edu.turing.model.dto.FlightDto;
import az.edu.turing.model.enums.FlightStatus;
import az.edu.turing.model.request.flight.CreateFlightRequest;
import az.edu.turing.model.request.flight.UpdateFlightRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final UserRepository userRepository;
    private FlightEntity existingFlight;
    private final FlightMapper flightMapper;
    private final FlightRepository flightRepository;

    public Page<FlightDto> findAll(String departure, String destination, LocalDateTime departureTime,
                                   LocalDateTime arrivalTime, Pageable pageable) {
        Specification<FlightEntity> spec = FlightSpecification.byFilters(departure, destination, departureTime, arrivalTime);
        Page<FlightEntity> flights = flightRepository.findAll(spec, pageable);
        return flights.map(flightMapper::toDto);
    }

    public Page<FlightDto> findAllIn24Hours(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twentyFourHoursLater = now.plusHours(24);
        Specification<FlightEntity> spec = FlightSpecification.byFilters(null, null, now, twentyFourHoursLater);
        Page<FlightEntity> flights = flightRepository.findAll(spec, pageable);
        return flights.map(flightMapper::toDto);
    }

    public FlightDto findById(Long id) {
        FlightEntity flightEntity = getFlightEntity(id);
        return flightMapper.toDto(flightEntity);
    }

    public FlightDto findByFlightNumber(String flightNumber) {
        FlightEntity flight = flightRepository.findByFlightNumber(flightNumber);
        if (flight == null) {
            throw new NotFoundException("Flight not found");
        }
        return flightMapper.toDto(flight);
    }

    public FlightDto create(Long userId, CreateFlightRequest flightRequest) {
        checkUserRole(userId);

        if (flightRepository.existsByFlightNumber(flightRequest.getFlightNumber())) {
            throw new AlreadyExistsException("Flight with number " + flightRequest.getFlightNumber() +
                    " already exists");
        }
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found for id: " + userId));
        FlightEntity flightEntity = flightMapper.toEntity(flightRequest);
        flightEntity.setCreatedBy(user);
        flightEntity.setUpdatedBy(user);
        flightEntity = flightRepository.save(flightEntity);

        return flightMapper.toDto(flightEntity);
    }

    public FlightDto update(Long userId, Long id, UpdateFlightRequest flightRequest) {
        checkUserRole(userId);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found for id: " + userId));
        if (flightRequest.getDeparture() == null || flightRequest.getDestination() == null || flightRequest.getDepartureTime() == null) {
            throw new InvalidInputException("Departure, destination, and departure time must be provided.");
        }
        existingFlight = getFlightEntity(id);
        existingFlight.setUpdatedBy(user);
        existingFlight.setDestination(flightRequest.getDestination());
        existingFlight.setDeparture(flightRequest.getDeparture());
        existingFlight.setDepartureTime(flightRequest.getDepartureTime());

        flightRepository.save(existingFlight);

        return flightMapper.toDto(existingFlight);
    }

    public FlightDto updateFlightNumber(Long userId, Long id, String flightNumber) {
        checkUserRole(userId);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found for id: " + userId));
        existingFlight = getFlightEntity(id);
        existingFlight.setUpdatedBy(user);
        existingFlight.setFlightNumber(flightNumber);

        flightRepository.save(existingFlight);

        return flightMapper.toDto(existingFlight);
    }

    public FlightDto updateFlightStatus(Long userId, Long id, FlightStatus flightStatus) {
        checkUserRole(userId);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found for id: " + userId));
        existingFlight = getFlightEntity(id);
        existingFlight.setUpdatedBy(user);
        existingFlight.setFlightStatus(flightStatus);

        flightRepository.save(existingFlight);

        return flightMapper.toDto(existingFlight);
    }

    public void decrementAvailableSeats(Long flightId) {
        existingFlight = getFlightEntity(flightId);
        flightRepository.decrementAvailableSeats(flightId);
    }

    public void delete(Long userId, Long id) {
        checkUserRole(userId);
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found for id: " + userId));
        existingFlight = getFlightEntity(id);
        existingFlight.setUpdatedBy(user);
        existingFlight.setFlightStatus(FlightStatus.CANCELLED);

        flightRepository.save(existingFlight);
    }

    private void checkUserRole(Long userId) {
        if(!userRepository.isAdmin(userId)) {
            throw new InvalidInputException("You can not get all users infos");
        }
    }

    private boolean existsById(Long id) {
        return flightRepository.existsById(id);
    }

    private FlightEntity getFlightEntity(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Flight with id " + id + " not found."));
    }
}
