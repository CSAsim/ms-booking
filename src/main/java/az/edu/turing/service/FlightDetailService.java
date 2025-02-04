package az.edu.turing.service;

import az.edu.turing.domain.entity.FlightDetailEntity;
import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.domain.repository.FlightDetailRepository;
import az.edu.turing.domain.repository.FlightRepository;
import az.edu.turing.domain.repository.UserRepository;
import az.edu.turing.exception.NotFoundException;
import az.edu.turing.mapper.FlightDetailMapper;
import az.edu.turing.model.dto.FlightDetailDto;
import az.edu.turing.model.request.flightDetails.CreateFlightDetailRequest;
import az.edu.turing.model.request.flightDetails.UpdateFlightDetailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightDetailService {

    private final FlightDetailRepository flightDetailRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final FlightDetailMapper mapper;

    public Page<FlightDetailDto> findAll(Pageable pageable) {
        return mapper.toDto(flightDetailRepository.findAll(pageable));
    }

    public FlightDetailDto findByFlightId(Long flightId) {
        return mapper.toDto(flightDetailRepository.findByFlightId(flightId)
                .orElseThrow(() -> new NotFoundException("Flight not found for id: " + flightId)));
    }

    public FlightDetailDto create(Long userId, CreateFlightDetailRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found for id: " + userId));
        validateFlightExists(request.getFlightId());
        FlightDetailEntity entity = mapper.toEntity(request);
        entity.setCreatedBy(user);
        entity.setUpdatedBy(user);
        return mapper.toDto(flightDetailRepository.save(entity));
    }

    public FlightDetailDto updateByFlightId(Long userId, Long id, UpdateFlightDetailRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found for id: " + userId));
        validateFlightExists(request.getFlightId());
        validateFlightDetailsExists(id);
        FlightDetailEntity entity = mapper.toEntity(request);
        entity.setUpdatedBy(user);
        return mapper.toDto(flightDetailRepository.save(entity));
    }

    public void deleteByFlightId(Long userId, Long flightId) {
        validateUserExists(userId);
        validateFlightExists(flightId);
        flightDetailRepository.deleteByFlightId(flightId);
    }

    private void validateFlightDetailsExists(Long id) {
        if(!flightDetailRepository.existsById(id)) {
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
