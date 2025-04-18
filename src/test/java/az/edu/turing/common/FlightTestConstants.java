package az.edu.turing.common;

import az.edu.turing.domain.entity.FlightEntity;
import az.edu.turing.model.dto.FlightDto;
import az.edu.turing.model.enums.FlightStatus;
import az.edu.turing.model.request.flight.CreateFlightRequest;
import az.edu.turing.model.request.flight.UpdateFlightRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightTestConstants {

    Long ID = 1L;
    Long USER_ID = 1L;
    int PAGE = 1;
    int SIZE = 4;
    String SORT_BY = "id";
    String FLIGHT_NUMBER = "flightNumber";
    String DEPARTURE = "Baku";
    String DESTINATION = "Kiev";
    LocalDateTime DEPARTURE_TIME = LocalDateTime.parse("2025-02-08T11:14:11.0252262");
    LocalDateTime ARRIVAL_TIME = LocalDateTime.parse("2025-02-08T11:14:11.0252262");
    FlightStatus FLIGHT_STATUS = FlightStatus.PENDING;
    int AVAILABLE_SEATS = 17;
    String BASE_URL = "/api/v1/flights";

    Pageable PAGEABLE = PageRequest.of(PAGE, SIZE, Sort.by(SORT_BY));

    CreateFlightRequest CREATE_FLIGHT_REQUEST = CreateFlightRequest.builder()
            .departureLocation(DEPARTURE)
            .destinationLocation(DESTINATION)
            .departureTime(DEPARTURE_TIME)
            .flightNumber(FLIGHT_NUMBER)
            .availableSeats(AVAILABLE_SEATS)
            .build();
    UpdateFlightRequest UPDATE_FLIGHT_REQUEST = new UpdateFlightRequest(DEPARTURE, DESTINATION, DEPARTURE_TIME,
            AVAILABLE_SEATS);

    FlightEntity FLIGHT_ENTITY = FlightEntity.builder()
            .id(ID)
            .flightNumber(FLIGHT_NUMBER)
            .departureLocation(DEPARTURE)
            .destinationLocation(DESTINATION)
            .departureTime(DEPARTURE_TIME)
            .arrivalTime(DEPARTURE_TIME)
            .availableSeats(AVAILABLE_SEATS)
            .flightStatus(FLIGHT_STATUS)
            .build();

    FlightEntity FLIGHT_ENTITY_UPDATED = FlightEntity.builder()
            .id(ID)
            .flightNumber(FLIGHT_NUMBER)
            .departureLocation(DEPARTURE)
            .destinationLocation(DESTINATION)
            .departureTime(DEPARTURE_TIME)
            .arrivalTime(DEPARTURE_TIME)
            .availableSeats(AVAILABLE_SEATS)
            .flightStatus(FLIGHT_STATUS)
            .build();

    FlightDto FLIGHT_DTO = FlightDto.builder()
            .flightNumber(FLIGHT_NUMBER)
            .departureLocation(DEPARTURE)
            .destinationLocation(DESTINATION)
            .departureTime(DEPARTURE_TIME)
            .arrivalTime(DEPARTURE_TIME)
            .availableSeats(AVAILABLE_SEATS)
            .flightStatus(FLIGHT_STATUS)
            .build();

    FlightDto FLIGHT_DTO_STATUS_UPDATED = FlightDto.builder()
            .flightNumber(FLIGHT_NUMBER)
            .departureLocation(DEPARTURE)
            .destinationLocation(DESTINATION)
            .arrivalTime(DEPARTURE_TIME)
            .availableSeats(AVAILABLE_SEATS)
            .flightStatus(FlightStatus.COMPLETED)
            .build();

    FlightEntity FLIGHT_ENTITY_DELETED = FlightEntity.builder()
            .id(ID)
            .flightNumber(FLIGHT_NUMBER)
            .departureLocation(DEPARTURE)
            .destinationLocation(DESTINATION)
            .arrivalTime(DEPARTURE_TIME)
            .availableSeats(AVAILABLE_SEATS)
            .flightStatus(FLIGHT_STATUS)
            .build();

    Page<FlightEntity> FLIGHT_ENTITY_PAGE = new PageImpl<>(List.of(FLIGHT_ENTITY), PAGEABLE, 1);
    Page<FlightDto> FLIGHT_DTO_PAGE = new PageImpl<>(List.of(FLIGHT_DTO), PAGEABLE, 1);
}
