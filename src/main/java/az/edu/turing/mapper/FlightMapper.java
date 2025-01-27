package az.edu.turing.mapper;

import az.edu.turing.domain.entity.FlightEntity;
import az.edu.turing.model.FlightDto;
import az.edu.turing.model.UserDto;
import az.edu.turing.model.request.flight.CreateFlightRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FlightMapper extends EntityMapper<FlightDto, FlightEntity> {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    FlightDto toDto(FlightEntity flightEntity);

    FlightEntity toEntity(FlightDto dto);

    FlightEntity toEntity(CreateFlightRequest createFlightRequest);
}
