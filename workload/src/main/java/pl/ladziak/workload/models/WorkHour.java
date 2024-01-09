package pl.ladziak.workload.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "_hours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkHour { //todo: przemyśleć akceptowanie i odrzucanie
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "work_start")
    private LocalDateTime start;
    @Column(name = "work_end")
    private LocalDateTime end;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;
}
