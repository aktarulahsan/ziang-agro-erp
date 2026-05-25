package com.agroerp.controller;

import com.agroerp.dto.UserDtos.UserRequest;
import com.agroerp.dto.UserDtos.UserResponse;
import com.agroerp.entity.Company;
import com.agroerp.entity.Role;
import com.agroerp.entity.User;
import com.agroerp.exception.BusinessException;
import com.agroerp.repository.CompanyRepository;
import com.agroerp.repository.RoleRepository;
import com.agroerp.repository.UserRepository;
import com.agroerp.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('SUPER_ADMIN')")
public class UserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, RoleRepository roleRepository,
                          CompanyRepository companyRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    @Transactional(readOnly = true)
    public ApiResponse<List<UserResponse>> list() {
        return ApiResponse.ok("Users loaded", userRepository.findAll().stream().map(this::toResponse).toList());
    }

    @PostMapping
    @Transactional
    public ApiResponse<UserResponse> create(@Valid @RequestBody UserRequest request) {
        if (userRepository.existsByUsername(request.username())) throw new BusinessException("Username already exists");
        if (userRepository.existsByEmail(request.email())) throw new BusinessException("Email already exists");
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setFullName(request.fullName());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setActive(request.active());
        user.setCompany(resolveCompany(request.companyId()));
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(request.roleIds()));
        if (roles.isEmpty()) throw new BusinessException("At least one valid role is required");
        user.setRoles(roles);
        return ApiResponse.ok("User created", toResponse(userRepository.save(user)));
    }

    @GetMapping("/roles")
    public ApiResponse<List<Role>> roles() {
        return ApiResponse.ok("Roles loaded", roleRepository.findAll());
    }

    @PutMapping("/{id}/roles")
    @Transactional
    public ApiResponse<UserResponse> updateRoles(@PathVariable Long id, @RequestBody Set<Long> roleIds) {
        User user = userRepository.findById(id).orElseThrow(() -> new BusinessException("User not found"));
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(roleIds));
        if (roles.isEmpty()) throw new BusinessException("At least one valid role is required");
        user.setRoles(roles);
        return ApiResponse.ok("User roles updated", toResponse(userRepository.save(user)));
    }

    @PatchMapping("/{id}/status")
    @Transactional
    public ApiResponse<UserResponse> status(@PathVariable Long id, @RequestParam boolean active) {
        User user = userRepository.findById(id).orElseThrow(() -> new BusinessException("User not found"));
        user.setActive(active);
        return ApiResponse.ok("User status updated", toResponse(userRepository.save(user)));
    }

    private UserResponse toResponse(User user) {
        Set<String> roles = user.getRoles().stream().map(Role::getName).collect(Collectors.toSet());
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getFullName(),
                user.getCompany().getId(), user.getCompany().getCompanyName(), roles, user.isActive());
    }

    private Company resolveCompany(Long companyId) {
        if (companyId != null) {
            return companyRepository.findById(companyId).orElseThrow(() -> new BusinessException("Company not found"));
        }
        return companyRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new BusinessException("Company not found"));
    }
}
