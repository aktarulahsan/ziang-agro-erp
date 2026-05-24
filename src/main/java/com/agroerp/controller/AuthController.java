package com.agroerp.controller;

import com.agroerp.dto.AuthDtos.LoginRequest;
import com.agroerp.dto.AuthDtos.LoginResponse;
import com.agroerp.entity.User;
import com.agroerp.exception.BusinessException;
import com.agroerp.repository.UserRepository;
import com.agroerp.response.ApiResponse;
import com.agroerp.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findActiveWithCompanyByUsername(principal.getUsername())
                .orElseThrow(() -> new BusinessException("User is missing an active company assignment"));
        Set<String> roles = principal.getAuthorities().stream().map(Object::toString).collect(Collectors.toSet());
        return ApiResponse.ok("Login successful", new LoginResponse(jwtService.generateToken(principal, user.getCompany().getId()), "Bearer",
                principal.getUsername(), user.getCompany().getId(), user.getCompany().getCompanyName(), roles));
    }
}
