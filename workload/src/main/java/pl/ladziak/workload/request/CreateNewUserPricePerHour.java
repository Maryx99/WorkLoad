package pl.ladziak.workload.request;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;

public record CreateNewUserPricePerHour(@DecimalMin(value = "27.70", message = "Hourly rate can't be lower then ${min}") BigDecimal hourlyRate) {
}
