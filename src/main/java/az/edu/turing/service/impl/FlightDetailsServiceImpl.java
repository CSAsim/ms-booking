package az.edu.turing.service.impl;

import az.edu.turing.domain.entity.FlightDetailsEntity;
import az.edu.turing.domain.repository.FlightDetailsRepository;
import az.edu.turing.domain.repository.FlightRepository;
import az.edu.turing.domain.repository.UserRepository;
import az.edu.turing.exception.NotFoundException;
import az.edu.turing.mapper.FlightDetailsMapper;
import az.edu.turing.model.FlightDetailsDto;
import az.edu.turing.model.request.flightDetails.CreateFlightDetailsRequest;
import az.edu.turing.model.request.flightDetails.UpdateFlightDetailsRequest;
import az.edu.turing.service.FlightDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightDetailsServiceImpl implements FlightDetailsService {

    private final FlightDetailsRepository flightDetailsRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final FlightDetailsMapper mapper;

    @Override
    public Page<FlightDetailsDto> findAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return mapper.toDto(flightDetailsRepository.findAll(pageable));
    }

    @Override
    public FlightDetailsDto findByFlightId(Long flightId) {
        return mapper.toDto(flightDetailsRepository.findByFlightId(flightId)
                .orElseThrow(() -> new NotFoundException("Flight not found for id: " + flightId)));
    }

    @Override
    public FlightDetailsDto create(Long userId, CreateFlightDetailsRequest request) {
        validateUserExists(userId);
        validateFlightExists(request.getFlightId());
        FlightDetailsEntity entity = mapper.toEntity(request);
        entity.setCreatedBy(userId);
        entity.setUpdatedBy(userId);
        return mapper.toDto(flightDetailsRepository.save(entity));
    }

    @Override
    public FlightDetailsDto update(Long userId, Long id, UpdateFlightDetailsRequest request) {
        validateUserExists(userId);
        validateFlightDetailsExists(id);
        FlightDetailsEntity entity = mapper.toEntity(request);
        entity.setUpdatedBy(userId);
        return mapper.toDto(flightDetailsRepository.save(entity));
    }

    @Override
    public void delete(Long userId, Long id) {
        validateUserExists(userId);
        validateFlightDetailsExists(id);
        flightDetailsRepository.deleteById(id);
    }

    @Override
    public void deleteByFlightId(Long userId, Long flightId) {
        validateUserExists(userId);
        validateFlightExists(flightId);
        flightDetailsRepository.deleteByFlightId(flightId);
    }

    private void validateFlightDetailsExists(Long id) {
        if(!flightDetailsRepository.existsById(id)) {
            throw new NotFoundException("Flight details not found for id: " + id);
        }
    }

    private void validateUserExists(Long userId) {
        if(!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found for id: " + userId);
        }
    }

    private void validateFlightExists(Long flightId) {
        if(!flightRepository.existsById(flightId)) {
            throw new NotFoundException("Flight not found for id: " + flightId);
        }
    }
}
