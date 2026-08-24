package com.pensieve.adapters.in;

import com.pensieve.adapters.mappers.UserCreateRequest;
import com.pensieve.adapters.mappers.UserResponse;
import com.pensieve.adapters.mappers.UserUpdateRequest;
import com.pensieve.application.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Usuários", description = "Criação de usuários e gestão de informações do usuário.")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Atualiza um usuário",
            description = "Atualiza as informações de um usuário existente.")
    @ApiResponse(responseCode = "201", description = "Usuário atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos", content = @Content)
    public UserResponse update(@AuthenticationPrincipal UUID userId, @RequestBody UserUpdateRequest request) {
        return userService.update(userId, request.name(), request.email(), request.password());
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Exclui um usuário",
            description = "Exclui um usuário existente.")
    @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados da requisição inválidos", content = @Content)
    public void delete(@AuthenticationPrincipal UUID userId) {
        userService.delete(userId);
    }
}
