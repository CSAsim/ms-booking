package az.edu.turing.service;

import az.edu.turing.domain.entity.BookingEntity;
import az.edu.turing.domain.entity.FlightEntity;
import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.domain.repository.BookingRepository;
import az.edu.turing.domain.repository.FlightRepository;
import az.edu.turing.domain.repository.UserRepository;
import az.edu.turing.exception.InvalidInputException;
import az.edu.turing.exception.NotFoundException;
import az.edu.turing.mapper.BookingMapper;
import az.edu.turing.model.dto.BookingDto;
import az.edu.turing.model.enums.BookingStatus;
import az.edu.turing.model.request.booking.CreateBookingRequest;
import az.edu.turing.model.request.booking.UpdateBookingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;
    private final BookingMapper bookingMapper;

    public Page<BookingDto> findAll(Long userId, Pageable pageable) {
        checkIsAdmin(userId);
        return bookingMapper.toDto(
                bookingRepository.findAll(pageable)
        );
    }

    public Page<BookingDto> findAllByFlightId(Long userId, Long id, Pageable pageable) {
        checkIsAdmin(userId);
        existsByFlightId(id);
        return bookingMapper.toDto(
                bookingRepository.findAllByFlightId(id, pageable)
        );
    }

    public Page<BookingDto> findAllByPassengerId(Long id, Pageable pageable) {
        existsByUserId(id);
        return bookingMapper.toDto(
                bookingRepository.findAllByPassengerId(id, pageable)
        );
    }

    @Transactional
    public BookingDto createBooking(Long userId, CreateBookingRequest request) {
        existsByUserId(userId);
        UserEntity user = UserEntity.builder()
                .id(userId)
                .build();
        FlightEntity flight = flightRepository.findById(request.getFlightId())
                .orElseThrow(() -> new NotFoundException("Flight not found for id: " + request.getFlightId()));
        UserEntity passenger = userRepository.findById(request.getPassengerId())
                .orElseThrow(() -> new NotFoundException("Passenger not found for id: " + request.getPassengerId()));
        flightRepository.decrementAvailableSeats(request.getFlightId());
        BookingEntity booking = bookingMapper.toEntity(request);
        booking.setFlight(flight);
        booking.setUser(passenger);
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setCreatedBy(user);
        booking.setUpdatedBy(user);

        return bookingMapper.toDto(bookingRepository.save(booking));
    }

    @Transactional
    public BookingDto updateBooking(Long userId, Long id, UpdateBookingRequest request) {
        existsByUserId(userId);
        UserEntity user = UserEntity.builder()
                .id(userId)
                .build();
        return bookingRepository.findById(id)
                .map(bookingEntity -> {
                    bookingEntity.setSeatNumber(request.getSeatNumber());
                    bookingEntity.setBookingStatus(request.getBookingStatus());
                    bookingEntity.setTotalAmount(request.getTotalAmount());
                    bookingEntity.setUpdatedBy(user);
                    BookingEntity savedBookingEntity = bookingRepository.save(bookingEntity);
                    return bookingMapper.toDto(savedBookingEntity);
                })
                .orElseThrow(() -> new NotFoundException("Booking not found for id: " + id));
    }

    @Transactional
    public BookingDto updateBookingStatus(Long userId, Long id, BookingStatus bookingStatus) {
        checkIsAdmin(userId);
        existsByUserId(userId);
        UserEntity user = UserEntity.builder()
                .id(userId)
                .build();
        return bookingRepository.findById(id)
                .map(bookingEntity -> {
                            bookingEntity.setBookingStatus(bookingStatus);
                            bookingEntity.setUpdatedBy(user);
                            BookingEntity updatedBookingEntity = bookingRepository.save(bookingEntity);
                            return bookingMapper.toDto(updatedBookingEntity);
                        }
                )
                .orElseThrow(() -> new NotFoundException("Booking not found for id: " + id));
    }

    @Transactional
    public void deleteBooking(Long userId, Long id) {
        existsByUserId(userId);
        UserEntity user = UserEntity.builder()
                .id(userId)
                .build();
        existsById(id);
        bookingRepository.findById(id)
                .ifPresent(booking -> {
                    booking.setUpdatedBy(user);
                    booking.setBookingStatus(BookingStatus.CANCELLED);
                    bookingRepository.save(booking);
                });
    }

    private void checkIsAdmin(Long userId) {

        if(!userRepository.isAdmin(userId)) {
            throw new InvalidInputException("You can not get all users infos");
        }
    }

    private void existsByFlightId(Long id) {
        if (!flightRepository.existsById(id)) {
            throw new NotFoundException("Flight not found for id: " + id);
        }
    }

    private void existsByUserId(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Passenger not found for id: " + id);
        }
    }

    private void existsById(Long id) {
        if(!bookingRepository.existsById(id)) {
            throw new NotFoundException("Booking not found for id: " + id);
        }
    }
}
