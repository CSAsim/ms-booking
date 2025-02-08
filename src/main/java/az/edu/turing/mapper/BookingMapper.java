package az.edu.turing.mapper;

import az.edu.turing.domain.entity.BookingEntity;
import az.edu.turing.model.dto.BookingDto;
import az.edu.turing.model.request.booking.CreateBookingRequest;
import az.edu.turing.model.request.booking.UpdateBookingRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface BookingMapper extends EntityMapper<BookingDto, BookingEntity>{

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(source = "flight.departureTime", target = "departureTime")
    @Mapping(source = "flight.arrivalTime", target = "arrivalTime")
    @Mapping(source = "flight.id", target = "flightId")
    @Mapping(source = "user.id", target = "userId")
    BookingDto toDto(BookingEntity entity);

    List<BookingDto> toDto(List<BookingEntity> entity);

    default Page<BookingDto> toDto(Page<BookingEntity> bookingEntities) {
        return bookingEntities.map(this::toDto);
    }


    @Mapping(source = "flightId", target = "flight.id")
    @Mapping(source = "passengerId", target = "user.id")
    BookingEntity toEntity(CreateBookingRequest request);

    BookingEntity toEntity(UpdateBookingRequest request);
}
