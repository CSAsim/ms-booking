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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final FlightMapper flightMapper;
    private final FlightRepository flightRepository;

    @Override
    public List<FlightDto> findAll(String departure, String destination, LocalDateTime departureTime,
                                   LocalDateTime arrivalTime) {
        Specification<FlightEntity> spec = FlightSpecification.byFilters(departure, destination, departureTime,
                arrivalTime);
        List<FlightEntity> flights = flightRepository.findAll(spec);
        return flightMapper.toDtoList(flights);
    }

    @Override
    public List<FlightDto> findAllIn24Hours() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twentyFourHoursLater = now.plusHours(24);
        Specification<FlightEntity> spec = FlightSpecification.byFilters(null, null, now,
                twentyFourHoursLater);
        List<FlightEntity> flights = flightRepository.findAll(spec);
        return flightMapper.toDtoList(flights);
    }

    @Override
    public Optional<FlightDto> findById(Long id) {
        Optional<FlightEntity> flight = flightRepository.findById(id);
        return flight.map(flightMapper::toDto);
    }

    @Override
    public List<FlightDto> findByFlightNumber(String flightNumber) {
        List<FlightEntity> flight = flightRepository.findByFlightNumber(flightNumber);
        return flightMapper.toDtoList(flight);
    }

    @Override
    public FlightDto create(CreateFlightRequest flightRequest) {
        if(flightRepository.existsByFlightNumber(flightRequest.getFlightNumber())){
            throw new AlreadyExistsException("Flight with number " + flightRequest.getFlightNumber() +
                    " already exists");
        }
        FlightEntity flightEntity = flightMapper.toEntity(flightRequest);
        flightEntity = flightRepository.save(flightEntity);

        return flightMapper.toDto(flightEntity);
    }

    @Override
    public FlightDto update(Long id, UpdateFlightRequest flightRequest) {
        if (flightRequest.getDeparture() == null || flightRequest.getDestination() == null || flightRequest.getDepartureTime() == null) {
            throw new InvalidInputException("Departure, destination, and departure time must be provided.");
        }

        FlightEntity existingFlight = getFlightEntity(id);
        existingFlight.setDestination(flightRequest.getDestination());
        existingFlight.setDeparture(flightRequest.getDeparture());
        existingFlight.setDepartureTime(flightRequest.getDepartureTime());

        flightRepository.save(existingFlight);

        return flightMapper.toDto(existingFlight);
    }

    @Override
    public FlightDto updateFlightNumber(Long id, String flightNumber) {
        FlightEntity existingFlight = getFlightEntity(id);
        existingFlight.setFlightNumber(flightNumber);

        flightRepository.save(existingFlight);

        return flightMapper.toDto(existingFlight);
    }

    @Override
    public FlightDto updateFlightStatus(Long id, StatusMessage flightStatus) {
        FlightEntity existingFlight = getFlightEntity(id);
        existingFlight.setFlightStatus(flightStatus);

        flightRepository.save(existingFlight);

        return flightMapper.toDto(existingFlight);
    }

    @Override
    public void delete(Long id) {
        FlightEntity existingFlight = getFlightEntity(id);

        existingFlight.setFlightStatus(StatusMessage.CANCELLED);

        flightRepository.save(existingFlight);
    }

    @Override
    public boolean existsByFlightNumber(String flightNumber) {
        return flightRepository.existsByFlightNumber(flightNumber);
    }

    @Override
    public boolean existsById(Long id) {
        return flightRepository.existsById(id);
    }

    private FlightEntity getFlightEntity(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Flight with id " + id + " not found."));
    }
}
