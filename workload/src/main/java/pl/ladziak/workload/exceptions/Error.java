package pl.ladziak.workload.exceptions;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record Error(String message, int statusCode, HttpStatus httpStatus) {

}
