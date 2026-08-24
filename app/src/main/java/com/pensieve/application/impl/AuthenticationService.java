package com.pensieve.application.impl;

import com.pensieve.adapters.mappers.AuthResponse;
import com.pensieve.adapters.out.persistence.jpa.UsersJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class AuthenticationService {
    private final UsersJpaRepository users;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthenticationService(UsersJpaRepository users, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    @Transactional
    public AuthResponse login(String email, String password) {
        if (email == null || password == null) {
            throw invalidCredentials();
        }
        if (!Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$").matcher(email).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email format");
        }

        var user = users.findByEmail(email.trim().toLowerCase(Locale.ROOT))
                .filter(candidate -> passwordEncoder.matches(password, candidate.getPassword()))
                .orElseThrow(this::invalidCredentials);
        return new AuthResponse(tokenService.create(user), "Bearer", user.getId(), user.getName(), user.getEmail());
    }

    private ResponseStatusException invalidCredentials() {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }
}
