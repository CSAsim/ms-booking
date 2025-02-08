package az.edu.turing.mapper;

import az.edu.turing.domain.entity.FlightDetailEntity;
import az.edu.turing.model.dto.FlightDetailDto;
import az.edu.turing.model.request.flight.CreateFlightRequest;
import az.edu.turing.model.request.flightDetails.CreateFlightDetailRequest;
import az.edu.turing.model.request.flightDetails.UpdateFlightDetailRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface FlightDetailMapper {

    @Mapping(source = "flight.id", target = "flightId")
    FlightDetailDto toDto(FlightDetailEntity entity);

    List<FlightDetailDto> toDto(List<FlightDetailEntity> entities);

    default Page<FlightDetailDto> toDto(Page<FlightDetailEntity> flightDetailsDto) {
        return flightDetailsDto.map(this::toDto);
    }

    @Mapping(source = "flightId", target = "flight.id")
    FlightDetailEntity toEntity(CreateFlightDetailRequest request);

    FlightDetailEntity toEntity(CreateFlightRequest request);

    @Mapping(source = "flightId", target = "flight.id")
    FlightDetailEntity toEntity(UpdateFlightDetailRequest request);
}
