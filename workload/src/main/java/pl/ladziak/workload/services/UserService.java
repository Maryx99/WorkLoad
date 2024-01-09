package pl.ladziak.workload.services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.ladziak.workload.dto.UserDto;
import pl.ladziak.workload.models.User;
import pl.ladziak.workload.repositories.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    // implementujemy tutaj UserDetailsService - jest to interface stworzony przez Stringa, dzieki temu ze jest to
    // interface za pomoca polimorfizmu mozemy dostarczyc wlasna implementacja, potrzebujemy tego zeby wyciagnac
    // z naszej bazy danych odpowiedniego uzytkownika za pomoca  metody 'loadUserByUsername' (email)
    // i go zwrocic lub wyrzucic blad ze user nie istnieje
    private final UserRepository userRepository;

    public List<UserDto> getUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserDto)
                .toList();
    }

    public UserDto mapToUserDto(User user) {
        return new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getRole());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email) // uzywamy metody findByEmail
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username: %s not found.", email)));
        // jak user nie istnieje z danym emailem - wyrzucamy blad
    }
}
