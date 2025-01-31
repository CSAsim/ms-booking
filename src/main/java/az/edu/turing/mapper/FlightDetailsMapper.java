package az.edu.turing.mapper;

import az.edu.turing.domain.entity.FlightDetailsEntity;
import az.edu.turing.model.FlightDetailsDto;
import az.edu.turing.model.request.flightDetails.CreateFlightDetailsRequest;
import az.edu.turing.model.request.flightDetails.UpdateFlightDetailsRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FlightDetailsMapper {

    @Mapping(source = "flight.id", target = "flightId")
    FlightDetailsDto toDto(FlightDetailsEntity entity);

    List<FlightDetailsDto> toDto(List<FlightDetailsEntity> entities);

    default Page<FlightDetailsDto> toDto(Page<FlightDetailsEntity> flightDetailsDto) {
        return flightDetailsDto.map(this::toDto);
    }

    @Mapping(source = "flightId", target = "flight.id")
    FlightDetailsEntity toEntity(CreateFlightDetailsRequest request);

    @Mapping(source = "flightId", target = "flight.id")
    FlightDetailsEntity toEntity(UpdateFlightDetailsRequest request);
}
