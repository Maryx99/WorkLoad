package pl.ladziak.workload.request;

import java.time.LocalDate;

public record CreateOrderRequest(String title, String description, LocalDate from, LocalDate to) {
}
