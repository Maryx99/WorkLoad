package pl.ladziak.workload.request;

// dane za pomoca ktorych chcemy sie zalogowac
public record LoginRequest(String email, String password) {
}
