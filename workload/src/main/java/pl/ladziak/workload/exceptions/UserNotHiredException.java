package pl.ladziak.workload.exceptions;

public class UserNotHiredException extends RuntimeException{
    public UserNotHiredException(String message) {
        super(message);
    }
}
