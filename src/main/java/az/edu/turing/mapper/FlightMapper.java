package az.edu.turing.mapper;

import az.edu.turing.domain.entity.FlightEntity;
import az.edu.turing.model.dto.FlightDto;
import az.edu.turing.model.request.flight.CreateFlightRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface FlightMapper extends EntityMapper<FlightDto, FlightEntity> {

    FlightMapper INSTANCE = Mappers.getMapper(FlightMapper.class);

    FlightDto toDto(FlightEntity flightEntity);

    FlightEntity toEntity(FlightDto dto);

    default Page<FlightDto> toDto(Page<FlightEntity> flightEntities) {
        return flightEntities.map(this::toDto);
    }

    @Mapping(target = "totalSeats", ignore = true)
    @Mapping(target = "flightStatus", ignore = true)
    @Mapping(target = "arrivalTime", ignore = true)
    FlightEntity toEntity(CreateFlightRequest createFlightRequest);
}
