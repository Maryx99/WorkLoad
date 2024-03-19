package pl.ladziak.workload.response;

import lombok.Builder;

@Builder
public record WorkHourSummaryResponse(String userUuid, String firstName, String lastName, double workedHours) {
}
