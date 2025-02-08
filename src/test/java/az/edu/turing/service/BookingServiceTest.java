package az.edu.turing.service;

import az.edu.turing.domain.entity.BookingEntity;
import az.edu.turing.domain.repository.BookingRepository;
import az.edu.turing.domain.repository.FlightRepository;
import az.edu.turing.domain.repository.UserRepository;
import az.edu.turing.exception.NotFoundException;
import az.edu.turing.mapper.BookingMapper;
import az.edu.turing.model.dto.BookingDto;
import az.edu.turing.model.enums.BookingStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import static az.edu.turing.common.BookingTestConstants.BOOKING_DTO;
import static az.edu.turing.common.BookingTestConstants.BOOKING_ENTITY;
import static az.edu.turing.common.BookingTestConstants.BOOKING_ENTITY_3;
import static az.edu.turing.common.BookingTestConstants.BOOKING_ENTITY_PAGE;
import static az.edu.turing.common.BookingTestConstants.BOOKING_ENTITY_PAGE_2;
import static az.edu.turing.common.BookingTestConstants.CREATE_BOOKING_REQUEST;
import static az.edu.turing.common.BookingTestConstants.FLIGHT_ENTITY;
import static az.edu.turing.common.BookingTestConstants.FLIGHT_ID;
import static az.edu.turing.common.BookingTestConstants.ID;
import static az.edu.turing.common.BookingTestConstants.PAGEABLE;
import static az.edu.turing.common.BookingTestConstants.PASSENGER_ID;
import static az.edu.turing.common.BookingTestConstants.UPDATE_BOOKING_REQUEST;
import static az.edu.turing.common.BookingTestConstants.USER_ENTITY;
import static az.edu.turing.common.BookingTestConstants.USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private BookingMapper bookingMapper = BookingMapper.INSTANCE;

    @InjectMocks
    private BookingService bookingService;


    @Test
    void findAll_Should_ReturnSuccess() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(bookingRepository.findAll(PAGEABLE)).willReturn(BOOKING_ENTITY_PAGE);

        Page<BookingDto> result = bookingService.findAll(USER_ID, PAGEABLE);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(List.of(BOOKING_DTO), result.getContent());

        then(userRepository).should(times(1)).isAdmin(USER_ID);
        then(bookingRepository).should(times(1)).findAll(PAGEABLE);
    }

    @Test
    void findAllByFlightId_Should_ReturnSuccess() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(bookingRepository.findAllByFlightId(FLIGHT_ID, PAGEABLE)).willReturn(BOOKING_ENTITY_PAGE_2);
        given(flightRepository.existsById(FLIGHT_ID)).willReturn(true);

        Page<BookingDto> result = bookingService.findAllByFlightId(USER_ID, FLIGHT_ID, PAGEABLE);

        assertNotNull(result);
        assertEquals(List.of(BOOKING_DTO), result.getContent());
        assertFalse(result.isEmpty());

        then(userRepository).should(times(1)).isAdmin(USER_ID);
        then(flightRepository).should(times(1)).existsById(FLIGHT_ID);
        then(bookingRepository).should(times(1)).findAllByFlightId(FLIGHT_ID, PAGEABLE);
    }

    @Test
    void findAllByFlightId_Should_ThrowNotfoundException_WhenFlightIdNotFound() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(flightRepository.existsById(FLIGHT_ID)).willReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.findAllByFlightId(USER_ID, FLIGHT_ID, PAGEABLE));

        assertEquals("Flight not found for id: " + FLIGHT_ID, exception.getMessage());

        then(userRepository).should(times(1)).isAdmin(USER_ID);
        then(bookingRepository).should(never()).findAllByFlightId(FLIGHT_ID, PAGEABLE);
    }

    @Test
    void findAllByPassengerId_Should_ReturnSuccess() {
        given(bookingRepository.findAllByPassengerId(PASSENGER_ID, PAGEABLE)).willReturn(BOOKING_ENTITY_PAGE_2);
        given(userRepository.existsById(PASSENGER_ID)).willReturn(true);

        Page<BookingDto> result = bookingService.findAllByPassengerId(PASSENGER_ID, PAGEABLE);

        assertNotNull(result);
        assertEquals(List.of(BOOKING_DTO), result.getContent());
        assertFalse(result.isEmpty());

        then(userRepository).should(times(1)).existsById(PASSENGER_ID);
        then(bookingRepository).should(times(1)).findAllByPassengerId(PASSENGER_ID, PAGEABLE);
    }

    @Test
    void findAllByPassengerId_Should_ThrowNotfoundException_WhenPassengerIdNotFound() {
        given(userRepository.existsById(PASSENGER_ID)).willReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.findAllByPassengerId(PASSENGER_ID, PAGEABLE));

        assertEquals("Passenger not found for id: " + PASSENGER_ID, exception.getMessage());

        then(bookingRepository).should(never()).findAllByPassengerId(PASSENGER_ID, PAGEABLE);
    }

    @Test
    void createBooking_Should_ReturnSuccess() {
        given(bookingRepository.save(any(BookingEntity.class))).willReturn(BOOKING_ENTITY);
        given(flightRepository.findById(FLIGHT_ID)).willReturn(Optional.of(FLIGHT_ENTITY));
        given(userRepository.findById(PASSENGER_ID)).willReturn(Optional.of(USER_ENTITY));
        given(userRepository.existsById(USER_ID)).willReturn(true);

        BookingDto result = bookingService.createBooking(USER_ID, CREATE_BOOKING_REQUEST);
        result.setBookingStatus(BookingStatus.PENDING);

        assertNotNull(result);
        assertEquals(BOOKING_DTO, result);

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(flightRepository).should(times(1)).findById(FLIGHT_ID);
        then(userRepository).should(times(1)).findById(PASSENGER_ID);
        then(bookingRepository).should(times(1)).save(any(BookingEntity.class));
    }

    @Test
    void createBooking_Should_ThrowNotFoundException_When_FlightIdNotFound() {
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(flightRepository.findById(FLIGHT_ID)).willReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.createBooking(USER_ID, CREATE_BOOKING_REQUEST));

        assertEquals("Flight not found for id: " + FLIGHT_ID, exception.getMessage());

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(flightRepository).should(times(1)).findById(FLIGHT_ID);
        then(userRepository).should(never()).findById(PASSENGER_ID);
        then(bookingRepository).should(never()).save(BOOKING_ENTITY);
    }

    @Test
    void createBooking_Should_ThrowNotFoundException_When_PassengerIdNotFound() {
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(flightRepository.findById(FLIGHT_ID)).willReturn(Optional.of(FLIGHT_ENTITY));
        given(userRepository.findById(PASSENGER_ID)).willReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.createBooking(USER_ID, CREATE_BOOKING_REQUEST));

        assertEquals("Passenger not found for id: " + PASSENGER_ID, exception.getMessage());

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(flightRepository).should(times(1)).findById(FLIGHT_ID);
        then(userRepository).should(times(1)).findById(PASSENGER_ID);
        then(bookingRepository).should(never()).save(BOOKING_ENTITY);
    }

    @Test
    void updateBooking_Should_ReturnSuccess() {
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(bookingRepository.findById(ID)).willReturn(Optional.of(BOOKING_ENTITY));
        given(bookingRepository.save(BOOKING_ENTITY)).willReturn(BOOKING_ENTITY);
        given(bookingMapper.toDto(BOOKING_ENTITY)).willReturn(BOOKING_DTO);

        BookingDto result = bookingService.updateBooking(USER_ID, ID, UPDATE_BOOKING_REQUEST);

        assertNotNull(result);
        assertEquals(BOOKING_DTO, result);

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(bookingRepository).should(times(1)).findById(ID);
        then(bookingRepository).should(times(1)).save(BOOKING_ENTITY);
    }

    @Test
    void updateBooking_Should_ThrowNotFoundException_When_BookingIdNotFound() {
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(bookingRepository.findById(ID)).willReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.updateBooking(USER_ID, ID, UPDATE_BOOKING_REQUEST));

        assertEquals("Booking not found for id: " + ID, exception.getMessage());

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(bookingRepository).should(times(1)).findById(ID);
        then(bookingRepository).should(never()).save(BOOKING_ENTITY);
    }

    @Test
    void updateBookingStatus_Should_ReturnSuccess() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(bookingRepository.findById(ID)).willReturn(Optional.of(BOOKING_ENTITY));
        given(bookingRepository.save(BOOKING_ENTITY)).willReturn(BOOKING_ENTITY);
        given(bookingMapper.toDto(BOOKING_ENTITY)).willReturn(BOOKING_DTO);

        BookingDto result = bookingService.updateBookingStatus(USER_ID, ID, BookingStatus.COMPLETED);

        assertNotNull(result);
        assertEquals(BOOKING_DTO, result);

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(bookingRepository).should(times(1)).findById(ID);
        then(bookingRepository).should(times(1)).save(BOOKING_ENTITY);
    }

    @Test
    void updateBookingStatus_Should_ThrowNotFoundException_When_BookingIdNotFound() {
        given(userRepository.isAdmin(USER_ID)).willReturn(true);
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(bookingRepository.findById(ID)).willReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.updateBookingStatus(USER_ID, ID, BookingStatus.COMPLETED));

        assertEquals("Booking not found for id: " + ID, exception.getMessage());

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(bookingRepository).should(times(1)).findById(ID);
        then(bookingRepository).should(never()).save(BOOKING_ENTITY);
    }

    @Test
    void deleteBooking_Should_ReturnSuccess() {
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(bookingRepository.existsById(ID)).willReturn(true);
        given(bookingRepository.findById(ID)).willReturn(Optional.of(BOOKING_ENTITY_3));

        bookingService.deleteBooking(USER_ID, ID);

        assertEquals(BookingStatus.CANCELLED, BOOKING_ENTITY_3.getBookingStatus());

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(bookingRepository).should(times(1)).existsById(ID);
        then(bookingRepository).should(times(1)).findById(ID);
        then(bookingRepository).should(times(1)).save(BOOKING_ENTITY_3);
    }

    @Test
    void deleteBooking_Should_ThrowNotFoundException_When_BookingIdNotFound() {
        given(userRepository.existsById(USER_ID)).willReturn(true);
        given(bookingRepository.existsById(ID)).willReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> bookingService.deleteBooking(USER_ID, ID));

        assertEquals("Booking not found for id: " + ID, exception.getMessage());

        then(userRepository).should(times(1)).existsById(USER_ID);
        then(bookingRepository).should(times(1)).existsById(ID);
        then(bookingRepository).should(never()).findById(ID);
        then(bookingRepository).should(never()).save(BOOKING_ENTITY);
    }
}
