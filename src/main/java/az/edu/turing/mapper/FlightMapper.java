package az.edu.turing.mapper;

import az.edu.turing.domain.entity.BookingEntity;
import az.edu.turing.domain.entity.FlightEntity;
import az.edu.turing.model.BookingDto;
import az.edu.turing.model.FlightDto;
import az.edu.turing.model.request.flight.CreateFlightRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FlightMapper extends EntityMapper<FlightDto, FlightEntity> {

    FlightMapper INSTANCE = Mappers.getMapper(FlightMapper.class);

    FlightDto toDto(FlightEntity flightEntity);

    FlightEntity toEntity(FlightDto dto);

    @Mapping(target = "totalSeats", ignore = true)
    @Mapping(target = "flightStatus", ignore = true)
    @Mapping(target = "arrivalTime", ignore = true)
    FlightEntity toEntity(CreateFlightRequest createFlightRequest);
}
