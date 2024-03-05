package pl.ladziak.workload.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateOrderRequest(String title, String description, LocalDateTime from, LocalDateTime to) {
}
