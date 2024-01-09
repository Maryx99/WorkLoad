package pl.ladziak.workload.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.ladziak.workload.models.WorkHour;

import java.util.List;

public interface WorkHourRepository extends JpaRepository<WorkHour, Long> {
    @Query(nativeQuery = true, value = """
            select * from _hours where user_id = :id
            """)
    List<WorkHour> getWorkHoursByUserEmail(Long id);
}
