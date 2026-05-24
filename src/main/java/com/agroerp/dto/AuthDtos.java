package com.agroerp.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public final class AuthDtos {
    private AuthDtos() {}

    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}
    public record LoginResponse(String token, String tokenType, String username, Long companyId,
                                String companyName, Set<String> roles) {}
}
