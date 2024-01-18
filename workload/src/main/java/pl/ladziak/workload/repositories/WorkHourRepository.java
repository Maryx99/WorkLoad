package pl.ladziak.workload.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.ladziak.workload.models.WorkHour;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface WorkHourRepository extends JpaRepository<WorkHour, Long> {
    @Query(nativeQuery = true, value = """
            select * from _hours where user_id = :userId
            """)
    List<WorkHour> getWorkHoursByUserId(Long userId);

    @Query(value = """
            SELECT h FROM WorkHour h
            JOIN FETCH h.user u
            WHERE h.start BETWEEN :from AND :to
            """)
    List<WorkHour> getWorkHoursByStartIsBetween(LocalDateTime from, LocalDateTime to);
}
