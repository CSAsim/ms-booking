package az.edu.turing.service;

import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.domain.repository.FlightRepository;
import az.edu.turing.domain.repository.UserRepository;
import az.edu.turing.exception.AlreadyExistsException;
import az.edu.turing.exception.InvalidInputException;
import az.edu.turing.exception.NotFoundException;
import az.edu.turing.mapper.FlightMapper;
import az.edu.turing.model.dto.FlightDto;
import az.edu.turing.model.enums.FlightStatus;
import az.edu.turing.model.request.flight.UpdateFlightRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

import static az.edu.turing.common.FlightTestConstants.CREATE_FLIGHT_REQUEST;
import static az.edu.turing.common.FlightTestConstants.DEPARTURE;
import static az.edu.turing.common.FlightTestConstants.DEPARTURE_TIME;
import static az.edu.turing.common.FlightTestConstants.DESTINATION;
import static az.edu.turing.common.FlightTestConstants.FLIGHT_DTO;
import static az.edu.turing.common.FlightTestConstants.FLIGHT_DTO_PAGE;
import static az.edu.turing.common.FlightTestConstants.FLIGHT_ENTITY;
import static az.edu.turing.common.FlightTestConstants.FLIGHT_ENTITY_PAGE;
import static az.edu.turing.common.FlightTestConstants.FLIGHT_NUMBER;
import static az.edu.turing.common.FlightTestConstants.ID;
import static az.edu.turing.common.FlightTestConstants.PAGEABLE;
import static az.edu.turing.common.FlightTestConstants.UPDATE_FLIGHT_REQUEST;
import static az.edu.turing.common.FlightTestConstants.USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.times;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Spy
    private FlightMapper flightMapper;
    @Mock
    private FlightRepository flightRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FlightService flightService;

    @Test
    void findAll_ShouldReturnAllFlights() {
        given(flightRepository.findAll(any(Specification.class), eq(PAGEABLE))).willReturn(FLIGHT_ENTITY_PAGE);
        given(flightMapper.toDto(FLIGHT_ENTITY)).willReturn(FLIGHT_DTO);

        Page<FlightDto> result = flightService.findAll(DEPARTURE, DESTINATION, DEPARTURE_TIME, null,
                PAGEABLE);

        assertNotNull(result);
        assertEquals(FLIGHT_DTO_PAGE.getContent(), result.getContent());

        then(flightRepository).should(times(1)).findAll(any(Specification.class), eq(PAGEABLE));
    }

    @Test
    void findAll_ShouldReturnEmptyPage() {
        given(flightRepository.findAll(any(Specification.class), eq(PAGEABLE))).willReturn(Page.empty());

        Page<FlightDto> result = flightService.findAll(DEPARTURE, DESTINATION, null, null,
                PAGEABLE);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        then(flightRepository).should(times(1)).findAll(any(Specification.class), eq(PAGEABLE));
    }

    @Test
    void findAllIn24Hours_ShouldReturnFlights() {
        given(flightRepository.findAll(any(Specification.class), eq(PAGEABLE))).willReturn(FLIGHT_ENTITY_PAGE);
        given(flightMapper.toDto(FLIGHT_ENTITY)).willReturn(FLIGHT_DTO);

        Page<FlightDto> result = flightService.findAllIn24Hours(PAGEABLE);

        assertNotNull(result);
        assertEquals(FLIGHT_DTO_PAGE.getContent(), result.getContent());

        then(flightRepository).should(times(1)).findAll(any(Specification.class), eq(PAGEABLE));
    }


    @Test
    void findById_ShouldReturnFlight() {
        given(flightRepository.findById(ID)).willReturn(Optional.of(FLIGHT_ENTITY));
        given(flightMapper.toDto(FLIGHT_ENTITY)).willReturn(FLIGHT_DTO);

        FlightDto result = flightService.findById(ID);
        assertNotNull(result);
        assertEquals(FLIGHT_DTO, result);
        then(flightRepository).should(times(1)).findById(eq(ID));
        then(flightMapper).should(times(1)).toDto(FLIGHT_ENTITY);
    }

    @Test
    void findById_ShouldThrowExceptionWhenFlightNotFound() {
        given(flightRepository.findById(ID)).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> flightService.findById(ID));

        then(flightRepository).should(times(1)).findById(eq(ID));
    }


    @Test
    void findByFlightNumber_ShouldReturnFlight() {
        given(flightRepository.findByFlightNumber(FLIGHT_NUMBER)).willReturn(FLIGHT_ENTITY);
        given(flightMapper.toDto(FLIGHT_ENTITY)).willReturn(FLIGHT_DTO);

        FlightDto result = flightService.findByFlightNumber(FLIGHT_NUMBER);
        assertNotNull(result);
        assertEquals(FLIGHT_DTO, result);

        then(flightRepository).should(times(1)).findByFlightNumber(eq(FLIGHT_NUMBER));
        then(flightMapper).should(times(1)).toDto(FLIGHT_ENTITY);
    }

    @Test
    void findByFlightNumber_ShouldThrowExceptionWhenFlightNotFound() {
        given(flightRepository.findByFlightNumber(FLIGHT_NUMBER)).willReturn(null);

        assertThrows(NotFoundException.class, () -> flightService.findByFlightNumber(FLIGHT_NUMBER));

        then(flightRepository).should(times(1)).findByFlightNumber(eq(FLIGHT_NUMBER));
    }

    @Test
    void createFlight_ShouldCreateFlight() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(flightRepository.existsByFlightNumber(FLIGHT_NUMBER)).willReturn(false);
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(new UserEntity()));
        given(flightMapper.toEntity(CREATE_FLIGHT_REQUEST)).willReturn(FLIGHT_ENTITY);
        given(flightRepository.save(any())).willReturn(FLIGHT_ENTITY);
        given(flightMapper.toDto(FLIGHT_ENTITY)).willReturn(FLIGHT_DTO);

        FlightDto result = flightService.create(USER_ID, CREATE_FLIGHT_REQUEST);

        assertEquals(FLIGHT_DTO, result);
        then(flightRepository).should(times(1)).save(any());
    }

    @Test
    void createFlight_ShouldThrowExceptionWhenFlightAlreadyExists() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(flightRepository.existsByFlightNumber(FLIGHT_NUMBER)).willReturn(true);

        assertThrows(AlreadyExistsException.class, () -> flightService.create(USER_ID, CREATE_FLIGHT_REQUEST));

        then(flightRepository).should(never()).save(any());
    }

    @Test
    void updateFlight_ShouldUpdateFlight() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(flightRepository.findById(ID)).willReturn(Optional.of(FLIGHT_ENTITY));
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(new UserEntity()));
        given(flightRepository.save(FLIGHT_ENTITY)).willReturn(FLIGHT_ENTITY);
        given(flightMapper.toDto(FLIGHT_ENTITY)).willReturn(FLIGHT_DTO);

        FlightDto result = flightService.update(USER_ID, ID, UPDATE_FLIGHT_REQUEST);

        assertEquals(FLIGHT_DTO, result);
        then(flightRepository).should(times(1)).findById(ID);
        then(flightRepository).should(times(1)).save(FLIGHT_ENTITY);
        then(flightMapper).should(times(1)).toDto(FLIGHT_ENTITY);
    }

    @Test
    void updateFlight_ShouldThrowsExceptionWhenFlightNotFound() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(new UserEntity()));
        given(flightRepository.findById(ID)).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> flightService.update(USER_ID, ID, UPDATE_FLIGHT_REQUEST));

        then(flightRepository).should(times(1)).findById(ID);
    }

    @Test
    void update_ShouldThrowException_WhenInvalidInput() {
        UpdateFlightRequest invalidRequest = new UpdateFlightRequest(null, null,
                null, 0);

        assertThrows(InvalidInputException.class, () -> flightService.update(USER_ID, ID, invalidRequest));
    }

    @Test
    void updateFlightNumber_ShouldUpdateFlightNumber() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(flightRepository.findById(ID)).willReturn(Optional.of(FLIGHT_ENTITY));
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(new UserEntity()));
        given(flightRepository.save(FLIGHT_ENTITY)).willReturn(FLIGHT_ENTITY);
        given(flightMapper.toDto(FLIGHT_ENTITY)).willReturn(FLIGHT_DTO);

        FlightDto result = flightService.updateFlightNumber(USER_ID, ID, "new_flight_number");

        assertEquals(FLIGHT_DTO, result);

        then(flightRepository).should(times(1)).findById(ID);
        then(userRepository).should(times(1)).findById(USER_ID);
        then(userRepository).should(times(1)).isAdmin(USER_ID);
    }

    @Test
    void updateFlightNumber_ShouldThrowExceptionWhenFlightNotFound() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(flightRepository.findById(ID)).willReturn(Optional.empty());
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(new UserEntity()));

        assertThrows(NotFoundException.class, () -> flightService.updateFlightNumber(USER_ID, ID,
                "new_flight_number"));

       then(flightRepository).should(times(1)).findById(ID);
    }

    @Test
    void updateFlightStatus_ShouldUpdateFlightStatus() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(flightRepository.findById(ID)).willReturn(Optional.of(FLIGHT_ENTITY));
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(new UserEntity()));
        given(flightRepository.save(FLIGHT_ENTITY)).willReturn(FLIGHT_ENTITY);
        given(flightMapper.toDto(FLIGHT_ENTITY)).willReturn(FLIGHT_DTO);

        FlightDto result = flightService.updateFlightStatus(USER_ID, ID, FlightStatus.COMPLETED);

        assertNotNull(result);

        then(flightRepository).should(times(1)).findById(ID);
        then(userRepository).should(times(1)).findById(USER_ID);
        then(userRepository).should(times(1)).isAdmin(USER_ID);
    }

    @Test
    void updateFlightStatus_ShouldThrowExceptionWhenFlightNotFound() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(flightRepository.findById(ID)).willReturn(Optional.empty());
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(new UserEntity()));

        assertThrows(NotFoundException.class, () -> flightService.updateFlightStatus(USER_ID, ID,
                FlightStatus.COMPLETED));

       then(flightRepository).should(times(1)).findById(ID);
    }

    @Test
    void deleteFlight_ShouldDeleteFlight() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(new UserEntity()));
        given(flightRepository.findById(ID)).willReturn(Optional.of(FLIGHT_ENTITY));
        given(flightRepository.save(FLIGHT_ENTITY)).willReturn(FLIGHT_ENTITY);

        flightService.delete(USER_ID, ID);

        then(flightRepository).should(times(1)).findById(ID);
    }

    @Test
    void deleteFlight_ShouldThrowExceptionWhenFlightNotFound() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(userRepository.findById(USER_ID)).willReturn(Optional.of(new UserEntity()));
        given(flightRepository.findById(ID)).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> flightService.delete(USER_ID, ID));

        then(flightRepository).should(times(1)).findById(ID);
    }

}
