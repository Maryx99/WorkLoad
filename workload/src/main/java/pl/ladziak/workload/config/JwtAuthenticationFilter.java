package pl.ladziak.workload.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.ladziak.workload.services.JwtService;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
// tutaj jest nasz filter ktory bedzie wywolywany co kazde zapytanie (OncePerRequestFilter)
// sprawdzamy tutaj nasz token - czy jest poprawny, dane sie zgadzaja i czy nie wygasl
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_PREFIX = "Bearer "; // kazdy token JWT ma prefix 'Bearer '
    private static final String AUTHORIZATION = "Authorization"; // w tym naglowku jest przesylany owy token
    private final JwtService jwtService; // korzystamy z naszego serwisu do parsowania JWT
    private final UserDetailsService userDetailsService; // korzystamy z interface ale tak naprawde jest tutaj nasza implementacja
    // ktora wyciaga usera po emailu

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION); // pobieramy naglowek 'Authorization'
        String jwt = null;
        String email = null;
        // sprawdzamy czy naglowek w zapytaniu istnieje i czy zaczyna sie z prefixem 'Bearer '
        // jezeli nie to wywolujemy filterChain.doFilter(request, response);
        // wtedy mamy blad ze jwt token jest zly lub brak
        if (Objects.nonNull(authHeader) && authHeader.startsWith(TOKEN_PREFIX)) {
            jwt = authHeader.substring(TOKEN_PREFIX.length()); // wyciagamy token jwt z naglowka
            email = jwtService.extractEmail(jwt); // wyciagamy email usera
        }
        // jezeli email nie jest nullem i jest uzytkownik zautentykowany w kontekscie to nic nie robimy
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(email); // tak naprawde wyszukujemy po email
            if (jwtService.isTokenValid(jwt, userDetails)) { // sprawdzamy czy token ktory mamy jest poprawny
                // i zgadza sie z emailem

                //tworzymy token z username i haslem
                var authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // ustawiamy w kontekscie zautentykowanego usera (czyli naszego zalogowanego usera)
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        // wywolujemy nastepny filtr w lancuchu filtrow, jest tutaj uzyty design  pattern chain of responsibilities,
        // gdzie kazda kolejna odpowiedzialnosc wykonywana jest po sobie
        filterChain.doFilter(request, response);
    }
}
