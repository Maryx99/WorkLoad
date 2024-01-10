package pl.ladziak.workload.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

//klasa konfiguracyjna w ktorej konfigurujemy aspekty zwiazane z Security
@Configuration // adnotacja ze Springa, inforuje ze jest to klasa informacyjna i beda tutaj obiekty oznaczone jako @Bean
@EnableWebSecurity // wlaczamy security
@EnableMethodSecurity // za pomoca tej adnotacji wlaczamy takie adnotacje jak @PreAuthorize @PostAuthorize
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Bean // ta adnotacja sluzy za to zeby poinformowac Springa ze ma zarzadzac klasa oznaczona jako @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable) // wylaczamy CSRF
                .authorizeHttpRequests(requests -> requests.requestMatchers("/auth/**") // to oznacza, ze:
                        .permitAll()// wszystkie zapytania ktore maja w url poczatek '/auth/' sa dostepne dla kazdego uzytkownika
                        // nawet jak nie jest zalogowany - ma to sens bo chcemy udostepnic endpointy dla ludzi ktorzy
                        // a) chca stworzyc nowe konto
                        // b) chca sie zalogowac
                        .anyRequest()
                        .authenticated()) // w dalszej czesci mowimy ze kazdy inny request ma byc zautentykowany - czyli musimy juz byc zalogowani

                //ustawiamy sesje na STATELESS - token JWT ma to do siebie ze jest bezstanowy czyli nie przechowuje informacji o stanie sesji
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider()) // dodajemy naszego providera
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // dodajemy filtr jwt przed
        // filtr zwiazany z username i haslem
        return http.build(); //budujemy obiekt
    }

    @Bean
    // jest to provider - jest kilka roznych implementacji np: DaoAuthenticationProvider, sluza one do wskazania
    // sposobu pobrania danych o userze
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); // w tym przypadku mamy providera
        // ktory wyciaga info o userze za pomoca userDetailsService
        provider.setUserDetailsService(userDetailsService); // w takim razie przekazujemy go tutaj (jest to nasza implementacja wyciagajaca usera po emailu)
        provider.setPasswordEncoder(passwordEncoder()); // ustawiamy tez passwordEncodera by porownac hash hasla z bazy
        // z haslem ktore dostajemy od usera ktory probuje sie zalogowac
        return provider;
    }

    @Bean // sluzy do procesowania, autentykacji przekazanego usera
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean // nasz passwordEncoder - najbardziej polecanym jest BCryptPasswordEncoder przez swoje bezpieczenstwo
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
