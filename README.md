# Booking Web APP

# Tasks

## Nurcan (Flight Service)

### Methods:
- List<FlightEntity> findAll()
- List<FlightEntity> findAllInLast24Hours()
- Optional<FlightEntity> findById(Long id)
- Optional<FlightEntity> findByFlightNumber(String flightNumber)
- FlightEntity create(FlightEntity flightEntity)
- FlightEntity update (Long id, FlightEntity flightEntity)
- FlightEntity updateFlightNumber(Long id, String flightNumber)
- FlightEntity updateFlightStatus(Long id, StatusMessage flightStatus)
- void delete(Long id) — soft delete: set flightStatus DELETED.
- When the flight is deleted, the bookingStatus value of the bookings should also be DELETED
- boolean existsByFlightNumber(String flightNumber)
- boolean existsById(Long id)


## Javad (User Service)

### Methods:
- List<UserEntity> findAll()
- List<UserEntity> findAllByFlightId(Long flightId) — Write @Query in left join
- Optional<UserEntity> findById(Long id)
- Optional<UserEntity> findByEmail(String email)
- UserEntity create(UserEntity userEntity)
- UserEntity update (Long id, UserEntity userEntity)
- void delete(Long id) — soft delete: set isDeleted field true.
- When the user is deleted, the bookingStatus value of the bookings should also be DELETED
- boolean existsById(Long id)


## Ömer (FLight Details Service)

### Methods:
- List<FlightDetailsEntity> findAll()
- Optional<FlightDetailsEntity> findByFlightId(Long flightId) — flightId from Flight entity
- Optional<FlightDetailsEntity> findByFlightNumber(String flightNumber) — flightNumber from FlightEntity
- FlightDetailsEntity create(FlightDetailsEntity details)
- FlightDetailsEntity update (Long id, FlightDetailsEntity details)
- void delete(Long id) — Hard delete
- deleteByFlightId(Long flightId)
- When the flight is deleted, the bookingStatus value of the bookings should also be DELETED and
- delete flight details for flight id
- boolean existsById(Long id)

