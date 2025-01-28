package az.edu.turing.service;

import az.edu.turing.model.BookingDto;
import az.edu.turing.model.enums.StatusMessage;
import az.edu.turing.model.request.booking.CreateBookingRequest;
import az.edu.turing.model.request.booking.UpdateBookingRequest;

import java.util.List;

public interface BookingService {

    List<BookingDto> findAll();

    List<BookingDto> findAllByFlightId(Long id);

    List<BookingDto> findAllByPassengerId(Long id);

    BookingDto createBooking(Long userId, CreateBookingRequest request);

    BookingDto updateBooking(Long userId, Long id, UpdateBookingRequest request);

    BookingDto updateBookingStatus(Long userId, Long id, StatusMessage bookingStatus);

    void deleteBooking(Long userId, Long id);


}
