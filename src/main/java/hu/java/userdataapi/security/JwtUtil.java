package hu.java.userdataapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class JwtUtil {

    @Value("${jwt.expiration.minutes}")
    private int jwtExpirationMinutes;

    @Value("${jwt.secret}")
    private String secretString;

    private Key secretKey;

    @PostConstruct
    public void initKey() {
        byte[] keyBytes = java.util.Base64.getDecoder().decode(secretString);

        if (keyBytes.length < 32) {
            throw new IllegalArgumentException("JWT secret must be at least 256 bits (32 bytes)");
        }

        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public UsernamePasswordAuthenticationToken validateTokenAndExtractUserSpringToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        ArrayList<String> rolesList = claims.get("roles", ArrayList.class);
        List<SimpleGrantedAuthority> roles =
                rolesList.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        return new UsernamePasswordAuthenticationToken(
                claims.getSubject(),
                null, roles);
    }

    public String generateToken(Authentication authentication) {
        List<String> roles = authentication
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("roles", roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * jwtExpirationMinutes))
                .signWith(secretKey)
                .compact();
    }
}
