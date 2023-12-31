package pl.ladziak.workload.response;

import lombok.Builder;

@Builder // nasza odpowiedz po poprawnym zalogowaniu sie usera - zwracany jest tutaj token JWT ktory posluzy do sprawdzania czy
// user ma dostep do naszych zasobow
public record LoginResponse(String token) {
}
