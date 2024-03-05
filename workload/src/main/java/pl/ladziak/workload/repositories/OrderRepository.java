package pl.ladziak.workload.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.ladziak.workload.models.Order;
import pl.ladziak.workload.models.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> getOrdersByUsersInAndFromAfterAndToBefore(Set<User> user, LocalDateTime from, LocalDateTime to);

    Optional<Order> findByUuid(String uuid);

    @Query(value = """
                        SELECT o FROM Order o
                        JOIN FETCH o.users u
                        WHERE o.uuid = :uuid
            """)
    Optional<Order> findByUuidAndAssignedUsers(String uuid);

}
