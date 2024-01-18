package pl.ladziak.workload.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "user_price_per_hour_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserPricePerHourHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid;
    private boolean isActive;
    private BigDecimal hourlyRate;
    private LocalDate validFrom;
    private LocalDate validTo;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User user;
}
