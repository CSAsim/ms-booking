package az.edu.turing.domain.repository;

import az.edu.turing.domain.entity.FlightEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class FlightSpecification {

    private FlightSpecification() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Specification<FlightEntity> byFilters(String departure, String destination,
                                                        LocalDateTime departureTime, LocalDateTime arrivalTime) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();
            if (departure != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("departure"), departure));
            }
            if (destination != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("destination"), destination));
            }
            if (departureTime != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("departureTime"),
                        departureTime));
            }
            if (arrivalTime != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("arrivalTime"), arrivalTime));
            }
            return predicate;
        };
    }
}
