package az.edu.turing.service;

import az.edu.turing.model.BookingDto;
import az.edu.turing.model.enums.StatusMessage;
import az.edu.turing.model.request.booking.CreateBookingRequest;
import az.edu.turing.model.request.booking.UpdateBookingRequest;

import java.util.List;

public interface BookingService {

    List<BookingDto> findAll();
    List<BookingDto> findAllByFlightId(long id);
    List<BookingDto> findAllByPassengerId(long id);
    List<BookingDto> findAllByFlightNumber(String flightNumber);
    BookingDto createBooking(CreateBookingRequest request);
    BookingDto updateBooking(long id, UpdateBookingRequest request);
    BookingDto updateBookingStatus(long id, StatusMessage bookingStatus);
    void deleteBooking(long id);
    void existsByFlightId(long id);
    void existsByFlightNumber(String flightNumber);
    void existsByPassengerId(long id);


}
