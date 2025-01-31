package az.edu.turing.service;

import az.edu.turing.domain.entity.FlightEntity;
import az.edu.turing.domain.repository.FlightRepository;
import az.edu.turing.domain.repository.UserRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static az.edu.turing.common.BookingTestConstants.USER_ID;
import static az.edu.turing.common.FlightTestConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Spy
    private FlightMapper flightMapper;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FlightServiceImpl flightService;

    @Test
    void findAll_ShouldReturnAllFlights() {
        Page<FlightEntity> flightPage = new PageImpl<>(List.of(FLIGHT_ENTITY));
        when(flightRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(flightPage);
        when(flightMapper.toDto(any(FlightEntity.class))).thenReturn(FLIGHT_DTO);

        Page<FlightDto> result = flightService.findAll(DEPARTURE, DESTINATION, null, null,
                PAGEABLE);

        assertFalse(result.isEmpty());
        assertEquals(FLIGHT_DTO, result.getContent().get(0));

        verify(flightRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void findAll_ShouldReturnEmptyList() {
        Page<FlightEntity> emptyPage = Page.empty();
        when(flightRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(emptyPage);

        Page<FlightDto> result = flightService.findAll(DEPARTURE, DESTINATION, null, null,
                PAGEABLE);

        assertTrue(result.isEmpty());
        verify(flightRepository, times(1)).findAll(any(Specification.class),
                any(Pageable.class));
    }

    @Test
    void findAllIn24Hours_ShouldReturnFlights() {
        Page<FlightEntity> flightPage = new PageImpl<>(List.of(FLIGHT_ENTITY));
        when(flightRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(flightPage);
        when(flightMapper.toDto(any(FlightEntity.class))).thenReturn(FLIGHT_DTO);

        Page<FlightDto> result = flightService.findAllIn24Hours(PAGEABLE);

        assertFalse(result.isEmpty());
        assertEquals(FLIGHT_DTO, result.getContent().getFirst());

        verify(flightRepository, times(1)).findAll(any(Specification.class),
                any(Pageable.class));
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
        when(flightRepository.findByFlightNumber(FLIGHT_NUMBER)).thenReturn(FLIGHT_ENTITY);
        when(flightMapper.toDto(FLIGHT_ENTITY)).thenReturn(FLIGHT_DTO);

        FlightDto result = flightService.findByFlightNumber(FLIGHT_NUMBER);

        assertNotNull(result);
        assertEquals(FLIGHT_DTO, result);
        verify(flightRepository, times(1)).findByFlightNumber(FLIGHT_NUMBER);
    }

    @Test
    void findByFlightNumber_ShouldThrowExceptionWhenFlightNotFound() {
        when(flightRepository.findByFlightNumber(FLIGHT_NUMBER)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> flightService.findByFlightNumber(FLIGHT_NUMBER));

        verify(flightRepository, times(1)).findByFlightNumber(FLIGHT_NUMBER);
    }

    @Test
    void createFlight_ShouldCreateFlight() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
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
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        when(flightRepository.existsByFlightNumber(FLIGHT_NUMBER)).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> flightService.create(ID, CREATE_FLIGHT_REQUEST));

        verify(flightRepository, times(1)).existsByFlightNumber(FLIGHT_NUMBER);
    }

    @Test
    void updateFlight_ShouldUpdateFlight() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
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
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
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
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
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
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        when(flightRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> flightService.updateFlightNumber(1L, ID,
                "new_flight_number"));

        verify(flightRepository, times(1)).findById(ID);
    }

    @Test
    void updateFlightStatus_ShouldUpdateFlightStatus() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
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
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        when(flightRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> flightService.updateFlightStatus(1L, ID,
                StatusMessage.COMPLETED));

        verify(flightRepository, times(1)).findById(ID);
    }

    @Test
    void deleteFlight_ShouldDeleteFlight() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        when(flightRepository.findById(ID)).thenReturn(Optional.of(FLIGHT_ENTITY));
        when(flightRepository.save(FLIGHT_ENTITY)).thenReturn(FLIGHT_ENTITY);

        flightService.delete(USER_ID, ID);

        verify(flightRepository, times(1)).findById(ID);
    }

    @Test
    void deleteFlight_ShouldThrowExceptionWhenFlightNotFound() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        when(flightRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> flightService.delete(USER_ID, ID));

        verify(flightRepository, times(1)).findById(ID);
    }
}
