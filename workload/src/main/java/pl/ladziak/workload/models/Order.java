package pl.ladziak.workload.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid;
    private String title;
    private String description;
    @Column(name = "order_from")
    private LocalDate from;
    @Column(name = "order_to")
    private LocalDate to;
    @ManyToMany
    private Set<User> users;
}
