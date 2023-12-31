package pl.ladziak.workload.request;

import lombok.Builder;
import pl.ladziak.workload.models.Role;

// cialo zapytania zawierajace dane o uzytkowniku - rejestracja nowego uzytkownika
@Builder
public record RegisterRequest(String password, String firstName, String lastName, String email,
                              Role role) {
}
