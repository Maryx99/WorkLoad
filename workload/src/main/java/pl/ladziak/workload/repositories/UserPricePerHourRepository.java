package pl.ladziak.workload.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ladziak.workload.models.UserPricePerHourHistory;

import java.util.Optional;

public interface UserPricePerHourRepository extends JpaRepository<UserPricePerHourHistory, Long> {

    Optional<UserPricePerHourHistory> findByIsActiveTrue();
}
