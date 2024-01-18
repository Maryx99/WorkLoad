package pl.ladziak.workload.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.ladziak.workload.models.User;
import pl.ladziak.workload.repositories.UserRepository;
import pl.ladziak.workload.request.LoginRequest;
import pl.ladziak.workload.request.RegisterRequest;
import pl.ladziak.workload.response.LoginResponse;

import java.util.UUID;

@Service
public record AuthenticationService(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService,
        AuthenticationManager authenticationManager) {

    public void registerUser(RegisterRequest request) { // rejestrujemy usera
        userRepository.save(User.builder() // mapujemy RegisterRequest na User (nasza encja)
                .uuid(UUID.randomUUID().toString())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password())) // nalezy pamietac by zencodowac haslo
                .role(request.role())
                .isHired(true)
                .build());
    }

    public LoginResponse login(LoginRequest request) { // logujemy sie userem
        authenticationManager.authenticate( // sprawdzamy czy podany email i haslo sie zgadzaja
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );
        User user = userRepository.findByEmail(request.email()) // szukamy usera po email
                //jezeli nie istnieje rzucamy blad
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username: %s not found.", request.email())));
        return LoginResponse.builder()
                .token(jwtService.generateToken(user)) // generujemy token i zwracamy go
                .build();
    }
}
