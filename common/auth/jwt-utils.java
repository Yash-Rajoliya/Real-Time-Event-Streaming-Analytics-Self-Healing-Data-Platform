package com.platform.analytics.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Lightweight helper component for generating, parsing, and validating JWT tokens.
 */
public class JwtUtils {

    private final SecretKey secretKey;
    private final long expirationMillis;
    private final String issuer;

    public JwtUtils(String secret, long expirationMillis, String issuer) {
        Objects.requireNonNull(secret, "Secret key string cannot be null");
        if (secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalArgumentException("Secret key must be at least 256-bits (32 characters) long");
        }
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMillis = expirationMillis;
        this.issuer = issuer;
    }

    /**
     * Generates a signed JWT for a given subject and role authorization claims.
     */
    public String generateToken(String subject, List<String> roles, Map<String, Object> additionalClaims) {
        long nowMs = System.currentTimeMillis();
        Date now = new Date(nowMs);
        Date expiry = new Date(nowMs + expirationMillis);

        var builder = Jwts.builder()
                .setSubject(subject)
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("roles", roles);

        if (additionalClaims != null && !additionalClaims.isEmpty()) {
            builder.addClaims(additionalClaims);
        }

        return builder.signWith(secretKey, SignatureAlgorithm.HS256).compact();
    }

    /**
     * Extracts subject (userId/username) from token.
     */
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts non-expired roles from token claims.
     */
    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }

    /**
     * Validates token validity against a specific subject.
     */
    public boolean isTokenValid(String token, String expectedSubject) {
        try {
            final String subject = extractSubject(token);
            return (subject.equals(expectedSubject) && !isTokenExpired(token));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Checks if token signature is valid and not expired.
     */
    public boolean validateToken(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}