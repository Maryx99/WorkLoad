package pl.ladziak.workload.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ladziak.workload.models.WorkHour;

public interface WorkHourRepository extends JpaRepository<WorkHour, Long> {

}
