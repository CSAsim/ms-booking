package az.edu.turing.service.impl;

import az.edu.turing.domain.entity.BookingEntity;
import az.edu.turing.domain.entity.FlightEntity;
import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.domain.repository.BookingRepository;
import az.edu.turing.domain.repository.FlightRepository;
import az.edu.turing.domain.repository.UserRepository;
import az.edu.turing.exception.NotFoundException;
import az.edu.turing.mapper.BookingMapper;
import az.edu.turing.model.BookingDto;
import az.edu.turing.model.enums.StatusMessage;
import az.edu.turing.model.request.booking.CreateBookingRequest;
import az.edu.turing.model.request.booking.UpdateBookingRequest;
import az.edu.turing.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;
    private BookingMapper bookingMapper;

    @Override
    public List<BookingDto> findAll() {
        return bookingMapper.toDto(
                bookingRepository.findAll()
        );
    }

    @Override
    public List<BookingDto> findAllByFlightId(long id) {
        existsByFlightId(id);
        return bookingMapper.toDto(
                bookingRepository.findAllByFlightId(id)
        );
    }

    @Override
    public List<BookingDto> findAllByPassengerId(long id) {
        existsByPassengerId(id);
        return bookingMapper.toDto(
                bookingRepository.findAllByPassengerId(id)
        );
    }

    @Override
    public List<BookingDto> findAllByFlightNumber(String flightNumber) {
        existsByFlightNumber(flightNumber);
        return bookingMapper.toDto(
                bookingRepository.findAllByFlightNumber(flightNumber)
        );
    }

    @Override
    public BookingDto createBooking(CreateBookingRequest request) {
        FlightEntity flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new NotFoundException("Flight not found for id: " + request.getFlightId()));

        UserEntity passenger = userRepository.findById(request.getPassengerId())
                .orElseThrow(() -> new NotFoundException("Passenger not found for id:" + request.getPassengerId()));
        BookingEntity booking = bookingMapper.toEntity(request);
        booking.setFlight(flight);
        booking.setPassenger(passenger);
        booking.setBookingStatus(StatusMessage.PENDING);
        return null;
    }

    @Override
    public BookingDto updateBooking(long id, UpdateBookingRequest request) {

        return null;
    }

    @Override
    public BookingDto updateBookingStatus(long id, StatusMessage bookingStatus) {
        return null;
    }

    @Override
    public void deleteBooking(long id) {

    }

    @Override
    public void existsByFlightId(long id) {
        if(!bookingRepository.existsByFlightId(id)) {
            throw new NotFoundException("Booking not found with flight id:" + id);
        }
    }

    @Override
    public void existsByFlightNumber(String flightNumber) {
        if(!bookingRepository.existsByFlightNumber(flightNumber)) {
            throw new NotFoundException("Booking not found with flight number:" + flightNumber);
        }
    }

    @Override
    public void existsByPassengerId(long id) {
        if(!bookingRepository.existsByPassengerId(id)) {
            throw new NotFoundException("Booking not found with passenger id:" + id);
        }
    }
}
