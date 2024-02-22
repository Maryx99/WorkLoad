package pl.ladziak.workload.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record OrderDto(String uuid, String title, String description, LocalDate from, LocalDate to) {
}
