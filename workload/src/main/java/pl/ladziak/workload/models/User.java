package pl.ladziak.workload.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "app_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // zmieniam adnotacje na Lombok'a dla czytelnosci, mam nadzieje ze juz ci to dziala
public class User implements UserDetails { // UserDetails pochodzi ze Spring Security, dostarcza on podstawowe informacje
    // o uzytkowniku, na podstawie UserDetails odbywaja sie sprawdzenia czy dane uzytkownika sie zgadzaja
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uuid; // dodaje UUID - by wygenerowac UUID wystarczy wywolac UUID.randomUuid().toString()
    // 0ba017f7-779c-4a68-9fb6-a6e5dd7516e5 - tak wyglada losowy UUID
    // jest to po prostu losowy ciag znakow, w ktorym mamy praktycznie gwarancje ze przez cale zycie bedzie unikalny
    // z tego co pamietam to musialoby sie generowac miliardy UUID na sekunde przez dlugie lata by bylo 50% szans ze sie
    // trafi duplikat
    // dodaje go zebysmy napisali aplikacje tez poprawnie
    private String firstName;
    private String lastName;
    @Column(unique = true)// dodalem constraint unique - co sprawia ze email musi byc unikalny
    private String email;
    private String password;
    private boolean isHired;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToMany(mappedBy = "user")
    private List<WorkHour> workHours;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { // get dla roli uzytkownika w systemie
        // GrantedAuthority to sa po prostu role jakie ma uzytkownik
        // SimpleGrantedAuthority to implementacja GrantedAuthority
        // Konwencja w Springu jest taka ze kazda rola powinna miec prefix ROLE_
        // dlatego jest ttutaj ROLE_ + role.name()
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() { // ta metoda wskazuje za pomoca czego sie logujemy do systemu
        // w naszym przypadku jest to email dlatego zwracamy email
        return email;
    }


    // kolejne 4 metody sa do zarzadzania kontem - aktywacji itp. nie planujemy sie tym bawic wiec dajemy
    // ze konto od razu jest aktywowane, dlatego wszedzie zwracamy true
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
