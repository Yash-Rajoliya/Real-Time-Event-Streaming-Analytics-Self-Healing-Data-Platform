// services/auth-service/src/main/java/com/platform/auth/service/AuthService.java
package com.platform.auth.service;

import com.platform.analytics.common.auth.JwtUtils;
import com.platform.auth.controller.AuthController.AuthResponse;
import com.platform.auth.controller.AuthController.LoginRequest;
import com.platform.auth.controller.AuthController.RefreshTokenRequest;
import com.platform.auth.controller.AuthController.RegisterRequest;
import com.platform.auth.service.UserService.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final StringRedisTemplate redisTemplate;

    private static final long ACCESS_TOKEN_VALIDITY_MS = 3600000; // 1 Hour
    private static final long REFRESH_TOKEN_VALIDITY_MS = 604800000; // 7 Days
    private static final String BLACKLIST_PREFIX = "jwt:blacklisted:";
    private static final String REFRESH_PREFIX = "jwt:refresh:";

    public AuthResponse register(RegisterRequest request) {
        UserPrincipal user = userService.createUser(
                request.getUsername(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                List.of("ROLE_USER")
        );
        return generateTokens(user);
    }

    public AuthResponse login(LoginRequest request) {
        UserPrincipal user = userService.findByUsernameOrEmail(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return generateTokens(user);
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        
        if (!jwtUtils.validateToken(refreshToken)) {
            throw new BadCredentialsException("Invalid or expired refresh token");
        }

        String userId = jwtUtils.extractSubject(refreshToken);
        String storedToken = redisTemplate.opsForValue().get(REFRESH_PREFIX + userId);

        if (storedToken == null || !storedToken.equals(refreshToken)) {
            throw new BadCredentialsException("Refresh token revoked or expired");
        }

        UserPrincipal user = userService.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("User not found"));

        return generateTokens(user);
    }

    public void logout(String accessToken) {
        if (jwtUtils.validateToken(accessToken)) {
            String userId = jwtUtils.extractSubject(accessToken);
            
            // Blacklist the access token until its remaining expiration
            redisTemplate.opsForValue().set(
                    BLACKLIST_PREFIX + accessToken,
                    "true",
                    Duration.ofMillis(ACCESS_TOKEN_VALIDITY_MS)
            );

            // Revoke active refresh token
            redisTemplate.delete(REFRESH_PREFIX + userId);
        }
    }

    private AuthResponse generateTokens(UserPrincipal user) {
        String accessToken = jwtUtils.generateToken(
                user.getId(),
                user.getRoles(),
                Map.of("username", user.getUsername(), "email", user.getEmail())
        );

        String refreshToken = UUID.randomUUID().toString();

        // Store refresh token in Redis with TTL
        redisTemplate.opsForValue().set(
                REFRESH_PREFIX + user.getId(),
                refreshToken,
                Duration.ofMillis(REFRESH_TOKEN_VALIDITY_MS)
        );

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(ACCESS_TOKEN_VALIDITY_MS / 1000)
                .userId(user.getId())
                .build();
    }
}