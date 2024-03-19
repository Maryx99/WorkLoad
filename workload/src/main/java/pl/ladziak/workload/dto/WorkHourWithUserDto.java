package pl.ladziak.workload.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record WorkHourWithUserDto(String workHourUuid, LocalDateTime workHourStart, LocalDateTime workHourEnd, String userUuid, String firstName, String lastName, boolean accepted) {
}
