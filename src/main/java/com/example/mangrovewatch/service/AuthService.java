package com.example.mangrovewatch.service;

import com.example.mangrovewatch.dto.AuthResponse;
import com.example.mangrovewatch.dto.LoginRequest;
import com.example.mangrovewatch.dto.RegisterRequest;
import com.example.mangrovewatch.model.User;
import com.example.mangrovewatch.repository.UserRepository;
import com.example.mangrovewatch.security.JwtUtils;
import com.example.mangrovewatch.security.JwtUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        JwtUserDetails userPrincipal = (JwtUserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(authentication);

        return new AuthResponse(jwt, "Bearer", userPrincipal.getId(),
                userPrincipal.getUsername(), userPrincipal.getEmail(),
                "", "USER"); // You can get fullName from database if needed
    }

    public AuthResponse register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists!");
        }
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setFullName(registerRequest.getFullName());
        user.setLocation(registerRequest.getLocation());
        user.setPhone(registerRequest.getPhone());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        User savedUser = userRepository.save(user);

        // Authenticate the newly registered user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerRequest.getEmail(),
                        registerRequest.getPassword()
                )
        );

        String jwt = jwtUtils.generateJwtToken(authentication);

        return new AuthResponse(jwt, "Bearer", savedUser.getId(),
                savedUser.getUsername(), savedUser.getEmail(),
                savedUser.getFullName(), savedUser.getRole().name());
    }
}
