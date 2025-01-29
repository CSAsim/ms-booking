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
    private final BookingMapper bookingMapper;

    @Override
    public List<BookingDto> findAll() {
        return bookingMapper.toDto(
                bookingRepository.findAll()
        );
    }

    @Override
    public List<BookingDto> findAllByFlightId(Long id) {
        existsByFlightId(id);
        return bookingMapper.toDto(
                bookingRepository.findAllByFlightId(id)
        );
    }

    @Override
    public List<BookingDto> findAllByPassengerId(Long id) {
        existsByPassengerId(id);
        return bookingMapper.toDto(
                bookingRepository.findAllByPassengerId(id)
        );
    }

    @Override
    public BookingDto createBooking(Long userId, CreateBookingRequest request) {
        existsByUserId(userId);
        FlightEntity flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new NotFoundException("Flight not found for id: " + request.getFlightId()));
        UserEntity passenger = userRepository.findById(request.getPassengerId())
                .orElseThrow(() -> new NotFoundException("Passenger not found for id: " + request.getPassengerId()));
        flightRepository.decrementAvailableSeats(request.getFlightId());
        BookingEntity booking = bookingMapper.toEntity(request);
        booking.setFlight(flight);
        booking.setPassenger(passenger);
        booking.setBookingStatus(StatusMessage.PENDING);
        booking.setCreatedBy(userId);
        booking.setUpdatedBy(userId);

        return bookingMapper.toDto(bookingRepository.save(booking));
    }


    @Override
    public BookingDto updateBooking(Long userId, Long id, UpdateBookingRequest request) {
        existsByUserId(userId);
        return bookingRepository.findById(id)
                .map(bookingEntity -> {
                    bookingEntity.setSeatNumber(request.getSeatNumber());
                    bookingEntity.setBookingStatus(request.getBookingStatus());
                    bookingEntity.setTotalAmount(request.getTotalAmount());
                    bookingEntity.setUpdatedBy(userId);
                    BookingEntity savedBookingEntity = bookingRepository.save(bookingEntity);
                    return bookingMapper.toDto(savedBookingEntity);
                })
                .orElseThrow(() -> new NotFoundException("Booking not found for id: " + id));
    }

    @Override
    public BookingDto updateBookingStatus(Long userId, Long id, StatusMessage bookingStatus) {

        existsByUserId(userId);
        return bookingRepository.findById(id)
                .map(bookingEntity -> {
                            bookingEntity.setBookingStatus(bookingStatus);
                            bookingEntity.setUpdatedBy(userId);
                            BookingEntity updatedBookingEntity = bookingRepository.save(bookingEntity);
                            return bookingMapper.toDto(updatedBookingEntity);
                        }
                )
                .orElseThrow(() -> new NotFoundException("Booking not found for id: " + id));
    }

    @Override
    public void deleteBooking(Long userId, Long id) {
        existsByUserId(userId);
        existsById(id);
        bookingRepository.findById(id)
                .ifPresent(booking -> {
                    booking.setBookingStatus(StatusMessage.CANCELLED);
                    bookingRepository.save(booking);
                });
    }

    private void existsByFlightId(Long id) {
        if (!flightRepository.existsById(id)) {
            throw new NotFoundException("Flight not found for id: " + id);
        }
    }

    private void existsByPassengerId(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Passenger not found for id: " + id);
        }
    }

    private void existsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found for id: " + userId);
        }
    }

    private void existsById(Long id) {
        if(!bookingRepository.existsById(id)) {
            throw new NotFoundException("Booking not found for id: " + id);
        }
    }
}
