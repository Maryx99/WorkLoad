package pl.ladziak.workload.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "hours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkHour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;
}
