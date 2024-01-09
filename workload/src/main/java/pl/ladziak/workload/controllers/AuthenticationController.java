package pl.ladziak.workload.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.ladziak.workload.request.LoginRequest;
import pl.ladziak.workload.request.RegisterRequest;
import pl.ladziak.workload.response.LoginResponse;
import pl.ladziak.workload.services.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
// zwykly controller do logowania i rejestracji
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    // metoda uzywana do rejestracji
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authenticationService.registerUser(request);
        return ResponseEntity.ok().build();
    }

    // metoda uzywana do logowania
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
}
