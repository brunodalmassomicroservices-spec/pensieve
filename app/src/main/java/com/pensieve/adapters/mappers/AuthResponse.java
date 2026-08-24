package com.pensieve.adapters.mappers;

import java.util.UUID;

public record AuthResponse(String accessToken, String tokenType, UUID userId, String name, String email) {
}
