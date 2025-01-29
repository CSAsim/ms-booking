package az.edu.turing.service;

import az.edu.turing.domain.entity.FlightEntity;
import az.edu.turing.domain.repository.FlightRepository;
import az.edu.turing.exception.AlreadyExistsException;
import az.edu.turing.exception.InvalidInputException;
import az.edu.turing.exception.NotFoundException;
import az.edu.turing.mapper.FlightMapper;
import az.edu.turing.model.FlightDto;
import az.edu.turing.model.enums.StatusMessage;
import az.edu.turing.model.request.flight.UpdateFlightRequest;
import az.edu.turing.service.impl.FlightServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static az.edu.turing.common.FlightTestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Spy
    private FlightMapper flightMapper;

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightServiceImpl flightService;

    @Test
    void findAll_ShouldReturnAllFlights() {
        when(flightRepository.findAll(any(Specification.class))).thenReturn(
                (List<FlightEntity>) List.of(FLIGHT_ENTITY));
        when(flightMapper.toDtoList(List.of(FLIGHT_ENTITY))).thenReturn(List.of(FLIGHT_DTO));

        List<FlightDto> result = flightService.findAll(DEPARTURE, DESTINATION, null,
                null);

        assertFalse(result.isEmpty());
        assertEquals(FLIGHT_DTO, result.getFirst());

        verify(flightRepository, times(1)).findAll(any(Specification.class));
        verify(flightMapper, times(1)).toDtoList(List.of(FLIGHT_ENTITY));
    }

    @Test
    void findAll_ShouldReturnEmptyList() {
        when(flightRepository.findAll(any(Specification.class))).thenReturn(Collections.emptyList());
        when(flightMapper.toDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<FlightDto> result = flightService.findAll(DEPARTURE, DESTINATION, null, null);

        assertTrue(result.isEmpty());
        verify(flightRepository, times(1)).findAll(any(Specification.class));
        verify(flightMapper, times(1)).toDtoList(Collections.emptyList());
    }

    @Test
    void findAllIn24Hours_ShouldReturnFlights() {
        when(flightRepository.findAll(any(Specification.class))).thenReturn(
                (List<FlightEntity>) List.of(FLIGHT_ENTITY));
        when(flightMapper.toDtoList(List.of(FLIGHT_ENTITY))).thenReturn(List.of(FLIGHT_DTO));

        List<FlightDto> result = flightService.findAllIn24Hours();

        assertFalse(result.isEmpty());
        assertEquals(FLIGHT_DTO, result.getFirst());

        verify(flightRepository, times(1)).findAll(any(Specification.class));
        verify(flightMapper, times(1)).toDtoList(List.of(FLIGHT_ENTITY));
    }

    @Test
    void findById_ShouldReturnFlight() {
        when(flightRepository.findById(ID)).thenReturn(Optional.of(FLIGHT_ENTITY));
        when(flightMapper.toDto(FLIGHT_ENTITY)).thenReturn(FLIGHT_DTO);

        FlightDto result = flightService.findById(ID);

        assertEquals(FLIGHT_DTO, result);
        verify(flightRepository, times(1)).findById(ID);
        verify(flightMapper, times(1)).toDto(FLIGHT_ENTITY);
    }

    @Test
    void findById_ShouldThrowExceptionWhenFlightNotFound() {
        when(flightRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> flightService.findById(ID));

        verify(flightRepository, times(1)).findById(ID);
    }

    @Test
    void findByFlightNumber_ShouldReturnFlight() {
        when(flightRepository.findByFlightNumber(FLIGHT_NUMBER)).thenReturn(List.of(FLIGHT_ENTITY));
        when(flightMapper.toDtoList(List.of(FLIGHT_ENTITY))).thenReturn(List.of(FLIGHT_DTO));

        List<FlightDto> result = flightService.findByFlightNumber(FLIGHT_NUMBER);

        assertFalse(result.isEmpty());
        assertEquals(FLIGHT_DTO, result.getFirst());
        verify(flightRepository, times(1)).findByFlightNumber(FLIGHT_NUMBER);
    }

    @Test
    void findByFlightNumber_ShouldThrowExceptionWhenFlightNotFound() {
        when(flightRepository.findByFlightNumber(FLIGHT_NUMBER)).thenReturn(List.of());

        assertThrows(NotFoundException.class, () -> flightService.findByFlightNumber(FLIGHT_NUMBER));
        verify(flightRepository, times(1)).findByFlightNumber(FLIGHT_NUMBER);
    }

    @Test
    void createFlight_ShouldCreateFlight() {
        when(flightRepository.existsByFlightNumber(FLIGHT_NUMBER)).thenReturn(false);
        when(flightMapper.toEntity(CREATE_FLIGHT_REQUEST)).thenReturn(FLIGHT_ENTITY);
        when(flightRepository.save(FLIGHT_ENTITY)).thenReturn(FLIGHT_ENTITY);
        when(flightMapper.toDto(FLIGHT_ENTITY)).thenReturn(FLIGHT_DTO);

        FlightDto result = flightService.create(ID, CREATE_FLIGHT_REQUEST);

        assertEquals(FLIGHT_DTO, result);
        verify(flightRepository, times(1)).save(FLIGHT_ENTITY);
        verify(flightMapper, times(1)).toEntity(CREATE_FLIGHT_REQUEST);
        verify(flightMapper, times(1)).toDto(FLIGHT_ENTITY);
    }

    @Test
    void createFlight_ShouldThrowExceptionWhenFlightAlreadyExists() {
        when(flightRepository.existsByFlightNumber(FLIGHT_NUMBER)).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> flightService.create(ID, CREATE_FLIGHT_REQUEST));

        verify(flightRepository, times(1)).existsByFlightNumber(FLIGHT_NUMBER);
    }

    @Test
    void updateFlight_ShouldUpdateFlight() {
        when(flightRepository.findById(ID)).thenReturn(Optional.of(FLIGHT_ENTITY));
        when(flightRepository.save(FLIGHT_ENTITY)).thenReturn(FLIGHT_ENTITY);
        when(flightMapper.toDto(FLIGHT_ENTITY)).thenReturn(FLIGHT_DTO);

        FlightDto result = flightService.update(1L, ID, UPDATE_FLIGHT_REQUEST);

        assertEquals(FLIGHT_DTO, result);
        verify(flightRepository, times(1)).findById(ID);
        verify(flightRepository, times(1)).save(FLIGHT_ENTITY);
        verify(flightMapper, times(1)).toDto(FLIGHT_ENTITY);
    }

    @Test
    void updateFlight_ShouldThrowsExceptionWhenFlightNotFound() {
        when(flightRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> flightService.update(1L, ID, UPDATE_FLIGHT_REQUEST));

        verify(flightRepository, times(1)).findById(ID);
    }

    @Test
    void update_ShouldThrowException_WhenInvalidInput() {
        UpdateFlightRequest invalidRequest = new UpdateFlightRequest(null, null,
                null, 0);

        assertThrows(InvalidInputException.class, () -> flightService.update(1L, ID, invalidRequest));
    }


    @Test
    void updateFlightNumber_ShouldUpdateFlightNumber() {
        when(flightRepository.findById(ID)).thenReturn(Optional.of(FLIGHT_ENTITY));
        when(flightRepository.save(FLIGHT_ENTITY)).thenReturn(FLIGHT_ENTITY);
        when(flightMapper.toDto(FLIGHT_ENTITY)).thenReturn(FLIGHT_DTO);

        FlightDto result = flightService.updateFlightNumber(1L, ID, "new_flight_number");

        assertNotNull(result);

        verify(flightRepository, times(1)).findById(ID);
        verify(flightRepository, times(1)).save(FLIGHT_ENTITY);
        verify(flightMapper, times(1)).toDto(FLIGHT_ENTITY);
    }

    @Test
    void updateFlightNumber_ShouldThrowExceptionWhenFlightNotFound() {
        when(flightRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> flightService.updateFlightNumber(1L, ID,
                "new_flight_number"));

        verify(flightRepository, times(1)).findById(ID);
    }

    @Test
    void updateFlightStatus_ShouldUpdateFlightStatus() {
        when(flightRepository.findById(ID)).thenReturn(Optional.of(FLIGHT_ENTITY));
        when(flightRepository.save(FLIGHT_ENTITY)).thenReturn(FLIGHT_ENTITY);
        when(flightMapper.toDto(FLIGHT_ENTITY)).thenReturn(FLIGHT_DTO);

        FlightDto result = flightService.updateFlightStatus(1L, ID, StatusMessage.COMPLETED);

        assertNotNull(result);

        verify(flightRepository, times(1)).findById(ID);
        verify(flightRepository, times(1)).save(FLIGHT_ENTITY);
        verify(flightMapper, times(1)).toDto(FLIGHT_ENTITY);
    }

    @Test
    void updateFlightStatus_ShouldThrowExceptionWhenFlightNotFound() {
        when(flightRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> flightService.updateFlightStatus(1L, ID,
                StatusMessage.COMPLETED));

        verify(flightRepository, times(1)).findById(ID);
    }

    @Test
    void deleteFlight_ShouldDeleteFlight() {
        when(flightRepository.findById(ID)).thenReturn(Optional.of(FLIGHT_ENTITY));
        when(flightRepository.save(FLIGHT_ENTITY)).thenReturn(FLIGHT_ENTITY);

        flightService.delete(ID);

        verify(flightRepository, times(1)).findById(ID);
    }

    @Test
    void deleteFlight_ShouldThrowExceptionWhenFlightNotFound() {
        when(flightRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> flightService.delete(ID));

        verify(flightRepository, times(1)).findById(ID);
    }
}
