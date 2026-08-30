package com.pensieve.application.impl;

import com.pensieve.adapters.mappers.AuthResponse;
import com.pensieve.adapters.mappers.LoginRequest;
import com.pensieve.adapters.out.persistence.entity.UsersEntity;
import com.pensieve.adapters.out.persistence.jpa.UsersJpaRepository;
import com.pensieve.config.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UsersJpaRepository usersJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthenticationService(UsersJpaRepository usersJpaRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtTokenProvider tokenProvider) {
        this.usersJpaRepository = usersJpaRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    public AuthResponse login(LoginRequest request) {
        UsersEntity user = usersJpaRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuário ou senha inválidos"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new RuntimeException("Usuário ou senha inválidos");
        }

        // Gera o token de forma stateless e retorna diretamente ao front-end
        String token = tokenProvider.generateToken(user.getEmail(), user.getId());

        return new AuthResponse(token, "Bearer", user.getId(), user.getName(), user.getEmail());
    }
}