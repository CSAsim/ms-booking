package az.edu.turing.service;

import az.edu.turing.domain.entity.FlightDetailEntity;
import az.edu.turing.domain.entity.FlightEntity;
import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.domain.repository.FlightDetailRepository;
import az.edu.turing.domain.repository.FlightRepository;
import az.edu.turing.domain.repository.FlightSpecification;
import az.edu.turing.domain.repository.UserRepository;
import az.edu.turing.exception.AlreadyExistsException;
import az.edu.turing.exception.InvalidInputException;
import az.edu.turing.exception.NotFoundException;
import az.edu.turing.mapper.FlightDetailMapper;
import az.edu.turing.mapper.FlightMapper;
import az.edu.turing.model.dto.FlightDetailDto;
import az.edu.turing.model.dto.FlightDto;
import az.edu.turing.model.enums.FlightStatus;
import az.edu.turing.model.request.flight.CreateFlightRequest;
import az.edu.turing.model.request.flight.UpdateFlightRequest;
import az.edu.turing.model.request.flightDetails.CreateFlightDetailRequest;
import az.edu.turing.model.request.flightDetails.UpdateFlightDetailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FlightService {

    private final UserRepository userRepository;
    private FlightEntity existingFlight;
    private final FlightMapper flightMapper;
    private final FlightDetailMapper flightDetailMapper;
    private final FlightRepository flightRepository;
    private final FlightDetailRepository flightDetailRepository;

    public Page<FlightDto> findAll(String departure, String destination, LocalDateTime departureTime,
                                   LocalDateTime arrivalTime, Pageable pageable) {
        Specification<FlightEntity> spec = FlightSpecification.byFilters(departure, destination, departureTime,
                arrivalTime);
        Page<FlightEntity> flights = flightRepository.findAll(spec, pageable);
        return flights.map(flightMapper::toDto);
    }

    public Page<FlightDto> findAllIn24Hours(Pageable pageable) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twentyFourHoursLater = now.plusHours(24);
        Specification<FlightEntity> spec = FlightSpecification.byFilters(null, null, now,
                twentyFourHoursLater);
        Page<FlightEntity> flights = flightRepository.findAll(spec, pageable);
        return flights.map(flightMapper::toDto);
    }

    public FlightDto findById(Long id) {
        FlightEntity flightEntity = getFlightEntity(id);
        return flightMapper.toDto(flightEntity);
    }

    public FlightDetailDto findFlightDetailById(Long flightId) {
        return flightDetailMapper.toDto(flightDetailRepository.findByFlightId(flightId)
                .orElseThrow(() -> new NotFoundException("Flight not found for id: " + flightId)));
    }

    public FlightDto findByFlightNumber(String flightNumber) {
        return flightMapper.toDto(flightRepository.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new NotFoundException("Flight with number " + flightNumber + " not found")));
    }

    @Transactional
    public FlightDto create(Long userId, CreateFlightRequest flightRequest) {
        checkUserRole(userId);

        if (flightRepository.existsByFlightNumber(flightRequest.getFlightNumber())) {
            throw new AlreadyExistsException("Flight with number " + flightRequest.getFlightNumber() +
                    " already exists");
        }
        UserEntity user = getUserEntity(userId);
        FlightEntity flightEntity = flightMapper.toEntity(flightRequest);
        flightEntity.setCreatedBy(user);
        flightEntity.setUpdatedBy(user);
        flightEntity = flightRepository.save(flightEntity);

        FlightDetailEntity flightDetailEntity = FlightDetailEntity.builder()
                .airlineName(flightRequest.getAirlineName())
                .planeModel(flightRequest.getPlaneModel())
                .maxWeight(flightRequest.getMaxWeight())
                .flight(flightEntity)
                .build();
        flightDetailRepository.save(flightDetailEntity);

        return flightMapper.toDto(flightEntity);
    }

    @Transactional
    public FlightDetailDto createFlightDetail(Long userId, CreateFlightDetailRequest request) {
        checkUserRole(userId);
        validateFlightExists(request.getFlightId());

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found for id: " + userId));
        FlightDetailEntity entity = flightDetailMapper.toEntity(request);
        entity.setCreatedBy(user);
        entity.setUpdatedBy(user);

        return flightDetailMapper.toDto(flightDetailRepository.save(entity));
    }

    @Transactional
    public FlightDto update(Long userId, Long id, UpdateFlightRequest flightRequest) {
        checkUserRole(userId);
        UserEntity user = getUserEntity(userId);

        existingFlight = getFlightEntity(id);
        existingFlight.setUpdatedBy(user);
        existingFlight.setDestinationLocation(flightRequest.getDestination());
        existingFlight.setDepartureLocation(flightRequest.getDeparture());
        existingFlight.setDepartureTime(flightRequest.getDepartureTime());

        flightRepository.save(existingFlight);

        return flightMapper.toDto(existingFlight);
    }
    @Transactional
    public FlightDetailDto updateFlightDetail(Long userId, Long id, UpdateFlightDetailRequest request) {
        checkUserRole(userId);
        validateFlightExists(request.getFlightId());
        validateFlightDetailsExists(id);

        UserEntity user = getUserEntity(userId);
        FlightDetailEntity entity = flightDetailMapper.toEntity(request);
        entity.setUpdatedBy(user);

        return flightDetailMapper.toDto(flightDetailRepository.save(entity));
    }

    @Transactional
    public FlightDto updateFlightNumber(Long userId, Long id, String flightNumber) {
        checkUserRole(userId);
        UserEntity user = getUserEntity(userId);
        existingFlight = getFlightEntity(id);
        existingFlight.setUpdatedBy(user);
        existingFlight.setFlightNumber(flightNumber);

        flightRepository.save(existingFlight);

        return flightMapper.toDto(existingFlight);
    }

    @Transactional
    public FlightDto updateFlightStatus(Long userId, Long id, FlightStatus flightStatus) {
        checkUserRole(userId);
        UserEntity user = getUserEntity(userId);
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

    @Transactional
    public void delete(Long userId, Long id) {
        checkUserRole(userId);
        UserEntity user = getUserEntity(userId);
        existingFlight = getFlightEntity(id);
        existingFlight.setUpdatedBy(user);
        existingFlight.setFlightStatus(FlightStatus.CANCELLED);

        flightRepository.save(existingFlight);
    }

    public void deleteFlightDetails(Long userId, Long flightId) {
        checkUserRole(userId);
        validateFlightExists(flightId);
        flightDetailRepository.deleteByFlightId(flightId);
    }

    private boolean existsByFlightNumber(String flightNumber) {
        return flightRepository.existsByFlightNumber(flightNumber);
    }

    private void checkUserRole(Long userId) {
        if (!userRepository.isAdmin(userId)) {
            throw new InvalidInputException("You can not get all users infos");
        }
    }

    private void validateFlightExists(Long flightId) {
        if (!flightRepository.existsById(flightId)) {
            throw new NotFoundException("Flight not found for id: " + flightId);
        }
    }

    private void validateFlightDetailsExists(Long id) {
        if (!flightDetailRepository.existsById(id)) {
            throw new NotFoundException("Flight details not found for id: " + id);
        }
    }

    private FlightEntity getFlightEntity(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Flight with id " + id + " not found."));
    }

    private UserEntity getUserEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found for id: " + userId));
    }
}
