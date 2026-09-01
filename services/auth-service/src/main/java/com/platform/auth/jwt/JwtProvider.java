// services/auth-service/src/main/java/com/platform/auth/jwt/JwtProvider.java
package com.platform.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private final SecretKey secretKey;
    private final long tokenValidityInMilliseconds;

    public JwtProvider(
            @Value("${jwt.secret:dGhpcy1pcy1hLXNlY3VyZS1kZWZhdWx0LXNlY3JldC1rZXktZm9yLWRldmVsb3BtZW50LXB1cnBvc2VzLW9ubHk=}") String secret,
            @Value("${jwt.expiration-ms:3600000}") long tokenValidityInMilliseconds) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.tokenValidityInMilliseconds = tokenValidityInMilliseconds;
    }

    public String createToken(String username, Collection<String> roles) {
        long now = System.currentTimeMillis();
        Date validity = new Date(now + tokenValidityInMilliseconds);

        return Jwts.builder()
                .setSubject(username)
                .claim("roles", String.join(",", roles))
                .setIssuedAt(new Date(now))
                .setExpiration(validity)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends SimpleGrantedAuthority> authorities =
                Arrays.stream(claims.get("roles").toString().split(","))
                        .filter(auth -> !auth.trim().isEmpty())
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}