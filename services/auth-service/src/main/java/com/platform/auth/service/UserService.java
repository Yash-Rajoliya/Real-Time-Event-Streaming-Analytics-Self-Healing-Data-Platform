// services/auth-service/src/main/java/com/platform/auth/service/UserService.java
package com.platform.auth.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory / Repository layer handling user identity lookup and credential storage.
 */
@Service
public class UserService {

    // Thread-safe repository map (In real production, wrap with Spring Data JPA/PostgreSQL)
    private final Map<String, UserPrincipal> usersById = new ConcurrentHashMap<>();
    private final Map<String, String> usernameToId = new ConcurrentHashMap<>();

    public UserPrincipal createUser(String username, String email, String encodedPassword, List<String> roles) {
        if (usernameToId.containsKey(username)) {
            throw new IllegalArgumentException("Username already exists: " + username);
        }

        String userId = "usr_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
        UserPrincipal user = new UserPrincipal(userId, username, email, encodedPassword, roles, true);

        usersById.put(userId, user);
        usernameToId.put(username, userId);
        usernameToId.put(email, userId);

        return user;
    }

    public Optional<UserPrincipal> findById(String userId) {
        return Optional.ofNullable(usersById.get(userId));
    }

    public Optional<UserPrincipal> findByUsernameOrEmail(String identifier) {
        String id = usernameToId.get(identifier);
        if (id == null) return Optional.empty();
        return findById(id);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserPrincipal {
        private String id;
        private String username;
        private String email;
        private String password;
        private List<String> roles;
        private boolean enabled;
    }
}