package pl.ladziak.workload.exceptions.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.ladziak.workload.exceptions.Error;
import pl.ladziak.workload.exceptions.UserNotHiredException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Error> handleRuntimeException(RuntimeException exception) {
        log.info("{}", exception);
        return ResponseEntity.badRequest().body(buildError(exception, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler({UsernameNotFoundException.class})
    public  ResponseEntity<Error> handleNotFoundException(RuntimeException exception) {
        return new ResponseEntity<>(buildError(exception, HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({UserNotHiredException.class})
    public  ResponseEntity<Error> handlerBadRequestException(RuntimeException exception) {
        return ResponseEntity.badRequest().body(buildError(exception, HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST));
    }

    private Error buildError(RuntimeException exception, int statusCode, HttpStatus httpStatus) {
       return Error.builder()
                .message(exception.getMessage())
                .statusCode(statusCode)
                .httpStatus(httpStatus)
                .build();
    }
}
