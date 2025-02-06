package az.edu.turing.common;

import az.edu.turing.domain.entity.FlightDetailEntity;
import az.edu.turing.domain.entity.FlightEntity;
import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.model.dto.FlightDetailDto;
import az.edu.turing.model.enums.UserRole;
import az.edu.turing.model.request.flightDetails.CreateFlightDetailRequest;
import az.edu.turing.model.request.flightDetails.UpdateFlightDetailRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageImpl;

import java.util.List;

public interface FlightDetailsConstants {

    Long ID = 1L;
    Long FLIGHT_ID = 1L;
    Long USER_ID = 1L;
    int PAGE = 1;
    int SIZE = 4;
    String SORT_BY = "id";
    String NAME = "user";
    String AIRLINE_NAME = "airline";
    String PLANE_MODEL = "mode1";
    Double MAX_WEIGHT = 33.0;

    FlightEntity FLIGHT_ENTITY = FlightEntity.builder()
            .id(FLIGHT_ID)
            .build();

    UserEntity USER_ENTITY = UserEntity.builder()
            .id(USER_ID)
            .name(NAME)
            .userRole(UserRole.USER)
            .build();

    FlightDetailEntity FLIGHT_DETAILS_ENTITY = FlightDetailEntity.builder()
            .id(ID)
            .airlineName(AIRLINE_NAME)
            .planeModel(PLANE_MODEL)
            .maxWeight(MAX_WEIGHT)
            .flight(FLIGHT_ENTITY)
            .build();

    FlightDetailDto FLIGHT_DETAILS_DTO = FlightDetailDto.builder()
            .id(ID)
            .airlineName(AIRLINE_NAME)
            .planeModel(PLANE_MODEL)
            .maxWeight(MAX_WEIGHT)
            .flightId(FLIGHT_ID)
            .build();

    CreateFlightDetailRequest CREATE_FLIGHT_DETAILS_REQUEST = CreateFlightDetailRequest.builder()
            .airlineName(AIRLINE_NAME)
            .planeModel(PLANE_MODEL)
            .maxWeight(MAX_WEIGHT)
            .flightId(FLIGHT_ID)
            .build();

    UpdateFlightDetailRequest UPDATE_FLIGHT_DETAILS_REQUEST = UpdateFlightDetailRequest.builder()
            .flightId(FLIGHT_ID)
            .airlineName(AIRLINE_NAME)
            .planeModel(PLANE_MODEL)
            .maxWeight(MAX_WEIGHT)
            .build();

    Pageable PAGEABLE = PageRequest.of(PAGE, SIZE, Sort.by(SORT_BY));
    Page<FlightDetailEntity> FLIGHT_DETAILS_ENTITY_PAGE = new PageImpl<>(List.of(FLIGHT_DETAILS_ENTITY),
            PAGEABLE, 1);
    Page<FlightDetailDto> FLIGHT_DETAILS_DTO_PAGE = new PageImpl<>(List.of(FLIGHT_DETAILS_DTO), PAGEABLE, 1);
}
