package az.edu.turing.mapper;

import az.edu.turing.domain.entity.BookingEntity;
import az.edu.turing.model.BookingDto;
import az.edu.turing.model.request.booking.CreateBookingRequest;
import az.edu.turing.model.request.booking.UpdateBookingRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingDto toDto(BookingEntity entity);
    List<BookingDto> toDto(List<BookingEntity> entity);

    BookingEntity toEntity(CreateBookingRequest request);
    BookingEntity tonEntity(UpdateBookingRequest request);
}
