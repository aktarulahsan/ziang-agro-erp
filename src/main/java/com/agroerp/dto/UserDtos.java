package com.agroerp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public final class UserDtos {
    private UserDtos() {}

    public record UserRequest(@NotBlank String username, @Email @NotBlank String email,
                              @NotBlank String fullName, @NotBlank String password,
                              Long companyId, @NotEmpty Set<Long> roleIds, boolean active) {}
    public record UserResponse(Long id, String username, String email, String fullName,
                               Long companyId, String companyName, Set<String> roles, boolean active) {}
}
