package com.pensieve.adapters.in;

import com.pensieve.adapters.mappers.AuthResponse;
import com.pensieve.adapters.mappers.LoginRequest;
import com.pensieve.adapters.mappers.UserCreateRequest;
import com.pensieve.adapters.mappers.UserResponse;
import com.pensieve.application.impl.AuthenticationService;
import com.pensieve.application.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticação", description = "Login e encerramento de sessão.")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final TokenService tokenService;
    private final UserService userService;

    public AuthenticationController(AuthenticationService authenticationService, TokenService tokenService, UserService userService) {
        this.authenticationService = authenticationService;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cria uma conta")
    public UserResponse register(@RequestBody UserCreateRequest request) {
        return userService.create(request.name(), request.email(), request.password());
    }

    @PostMapping("/login")
    @Operation(summary = "Autentica um usuário")
    @ApiResponse(responseCode = "200", description = "Login efetuado")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authenticationService.login(request.email(), request.password());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Encerra a sessão atual")
    public void logout(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            tokenService.revoke(header.substring(7).trim());
        }
    }
}
