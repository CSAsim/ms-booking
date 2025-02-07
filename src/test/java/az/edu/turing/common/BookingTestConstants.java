package az.edu.turing.common;

import az.edu.turing.domain.entity.BookingEntity;
import az.edu.turing.domain.entity.FlightEntity;
import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.model.dto.BookingDto;
import az.edu.turing.model.enums.BookingStatus;
import az.edu.turing.model.enums.UserRole;
import az.edu.turing.model.request.booking.CreateBookingRequest;
import az.edu.turing.model.request.booking.UpdateBookingRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public interface BookingTestConstants {

    Long ID = 1L;
    Long USER_ID = 2L;
    Long FLIGHT_ID = 2L;
    Long PASSENGER_ID = 2L;
    Integer PAGE = 1;
    Integer SIZE = 1;
    String SORT_BY = "id";
    Integer SEAT_NUMBER = 23;
    Double TOTAL_AMOUNT = 234.0;
    String NAME = "test_name";
    String BASE_URL = "/api/v1/bookings";

    Pageable PAGEABLE = PageRequest.of(PAGE, SIZE, Sort.by(SORT_BY));

    UserEntity USER_ENTITY = UserEntity.builder()
            .id(USER_ID)
            .name(NAME)
            .userRole(UserRole.USER)
            .build();

    FlightEntity FLIGHT_ENTITY = FlightEntity.builder()
            .id(FLIGHT_ID)
            .build();

    BookingEntity BOOKING_ENTITY = BookingEntity.builder()
            .id(ID)
            .flight(FLIGHT_ENTITY)
            .user(USER_ENTITY)
            .seatNumber(SEAT_NUMBER)
            .bookingStatus(BookingStatus.PENDING)
            .totalAmount(TOTAL_AMOUNT)
            .build();

    BookingEntity BOOKING_ENTITY_2 = BookingEntity.builder()
            .id(ID)
            .flight(FLIGHT_ENTITY)
            .user(USER_ENTITY)
            .seatNumber(SEAT_NUMBER)
            .bookingStatus(BookingStatus.PENDING)
            .totalAmount(TOTAL_AMOUNT)
            .build();

    BookingEntity BOOKING_ENTITY_3 = BookingEntity.builder()
            .id(ID)
            .flight(FLIGHT_ENTITY)
            .user(USER_ENTITY)
            .seatNumber(SEAT_NUMBER)
            .bookingStatus(BookingStatus.PENDING)
            .totalAmount(TOTAL_AMOUNT)
            .build();


    BookingDto BOOKING_DTO = BookingDto.builder()
            .flightId(FLIGHT_ID)
            .userId(PASSENGER_ID)
            .totalAmount(TOTAL_AMOUNT)
            .seatNumber(SEAT_NUMBER)
            .bookingStatus(BookingStatus.PENDING)
            .build();

    CreateBookingRequest CREATE_BOOKING_REQUEST = CreateBookingRequest.builder()
            .flightId(FLIGHT_ID)
            .passengerId(PASSENGER_ID)
            .seatNumber(SEAT_NUMBER)
            .totalAmount(TOTAL_AMOUNT)
            .build();

    UpdateBookingRequest UPDATE_BOOKING_REQUEST = UpdateBookingRequest.builder()
            .bookingStatus(BookingStatus.COMPLETED)
            .seatNumber(SEAT_NUMBER)
            .totalAmount(TOTAL_AMOUNT)
            .build();

    Page<BookingEntity> BOOKING_ENTITY_PAGE = new PageImpl<>(List.of(BOOKING_ENTITY), PAGEABLE, 1L);
    Page<BookingEntity> BOOKING_ENTITY_PAGE_2 = new PageImpl<>(List.of(BOOKING_ENTITY_2), PAGEABLE, 1L);
    Page<BookingDto> BOOKING_DTO_PAGE = new PageImpl<>(List.of(BOOKING_DTO), PAGEABLE, 1L);
}
