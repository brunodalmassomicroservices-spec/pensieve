package com.pensieve.adapters.mappers;

public record UserUpdateRequest(String name, String email, String password) {
}
