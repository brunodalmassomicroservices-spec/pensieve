package com.pensieve.application.impl;

import com.pensieve.adapters.mappers.UserResponse;
import com.pensieve.adapters.out.persistence.entity.UsersEntity;
import com.pensieve.adapters.out.persistence.jpa.ReviewJpaRepository;
import com.pensieve.adapters.out.persistence.jpa.TriggerJpaRepository;
import com.pensieve.adapters.out.persistence.jpa.UsersJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UsersJpaRepository usersRepository;
    private final TriggerJpaRepository triggerRepository;
    private final ReviewJpaRepository reviewRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UsersJpaRepository usersRepository,
                       TriggerJpaRepository triggerRepository,
                       ReviewJpaRepository reviewRepository,
                       PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.triggerRepository = triggerRepository;
        this.reviewRepository = reviewRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse create(String name, String email, String password) {
        var normalizedEmail = normalizeEmail(email);
        validateNameAndPassword(name, password);

        if (usersRepository.existsByEmail(normalizedEmail)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        var user = new UsersEntity();
        user.setId(UUID.randomUUID());
        user.setName(name.trim());
        user.setEmail(normalizedEmail);
        user.setPassword(passwordEncoder.encode(password));
        user.setCreatedAt(LocalDateTime.now());

        return toResponse(usersRepository.save(user));
    }

    @Transactional
    public UserResponse update(UUID id, String name, String password) {
        var user = findUser(id);
        validateName(name);

        user.setName(name.trim());
        user.setEmail(user.getEmail());

        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        return toResponse(usersRepository.save(user));
    }

    @Transactional
    public void delete(UUID id) {
        findUser(id);
        reviewRepository.deleteAllByUserId(id);
        triggerRepository.deleteAllByUserId(id);
        usersRepository.deleteById(id);
    }

    private UsersEntity findUser(UUID id) {
        return usersRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
        }
        if (!Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$").matcher(email).matches()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email format");
        }

        return email.trim().toLowerCase(Locale.ROOT);
    }

    private void validateNameAndPassword(String name, String password) {
        validateName(name);
        if (password == null || password.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required");
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        }
    }

    private UserResponse toResponse(UsersEntity user) {
        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getCreatedAt());
    }
}
