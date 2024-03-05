package pl.ladziak.workload.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.ladziak.workload.models.User;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret.key}") // ta adnotacja pozwala nam wstrzyknac wartosci z pliku application.properties
    // trzeba zastosowac sie do syntaxu czyli umiescic nazwe propertki do ktorej sie odnosimy w ${}
    // robi sie w taki sposob zeby mozna bylo latwo podmienic wartosci konfiguracyjne z pliku
    private String secretKey; // klucz do generowania tokenu
    @Value("${jwt.secret.expiration}")
    private Long expiration; // po jakim czasie token powinien byc invalid, np po 10 minutach, obecnie jest to godzina

    public String generateToken(User userDetails) {
        return generateToken(Map.of("uuid", userDetails.getUuid(), "role", userDetails.getRole()), userDetails);
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return buildToken(claims, userDetails, expiration);
    }

    private String buildToken(Map<String, Object> claims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .setClaims(claims) // zawieraja info o uzytkowniku, np email, email, role, kiedy token jest nieaktywny
                .setSubject(userDetails.getUsername()) // subject = zalogowany user
                .setIssuedAt(new Date()) // moment wygenerowania tokenu
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // moment kiedy token ma stac sie invalid
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // hashowanie tokenu
                .compact();
    }

    public String extractEmail(String token) { // wyciaga email
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) { // sprawdzamy czy token jest jeszcze aktywny
        final String email = userDetails.getUsername();

        return !isTokenExpired(token) && extractEmail(token).equals(email);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) { // metoda pomocnicza do wyciagania info
        // z tokenu o userze
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date extractExpirationDate(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return extractExpirationDate(token).before(new Date());
    }

    private Claims extractAllClaims(String token) { // wyciagniecie wszystkich info z tokenu
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() { // klucz za pomoca ktorego hashujemy token
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
