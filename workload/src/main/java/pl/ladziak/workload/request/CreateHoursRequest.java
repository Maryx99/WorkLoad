package pl.ladziak.workload.request;

import java.time.LocalDateTime;

public record CreateHoursRequest(LocalDateTime start, LocalDateTime end) {
}
