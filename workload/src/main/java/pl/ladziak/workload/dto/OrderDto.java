package pl.ladziak.workload.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record OrderDto(String uuid, String title, String description, LocalDateTime from, LocalDateTime to) {
}
