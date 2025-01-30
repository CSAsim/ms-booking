package az.edu.turing.service;

import az.edu.turing.model.BookingDto;
import az.edu.turing.model.enums.StatusMessage;
import az.edu.turing.model.request.booking.CreateBookingRequest;
import az.edu.turing.model.request.booking.UpdateBookingRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingService {

    Page<BookingDto> findAll(Pageable pageable);

    Page<BookingDto> findAllByFlightId(Long id, Pageable pageable);

    Page<BookingDto> findAllByPassengerId(Long id, Pageable pageable);

    BookingDto createBooking(Long userId, CreateBookingRequest request);

    BookingDto updateBooking(Long userId, Long id, UpdateBookingRequest request);

    BookingDto updateBookingStatus(Long userId, Long id, StatusMessage bookingStatus);

    void deleteBooking(Long userId, Long id);


}
