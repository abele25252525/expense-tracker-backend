package com.example.expensetracker.controller;

import com.example.expensetracker.config.JwtUtil;
import com.example.expensetracker.dto.LoginResponse;
import com.example.expensetracker.model.User;
import com.example.expensetracker.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(
            AuthService authService,
            AuthenticationManager authenticationManager,
            JwtUtil jwtUtil
    ) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        try {
            authService.register(user);
            return ResponseEntity.ok("Registered successfully");
        } catch (RuntimeException e) {
            // Duplicate email
            return ResponseEntity.status(409).body(e.getMessage());
        }
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            user.getPassword()
                    )
            );

            // Get user details and generate JWT token
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);

            // Return token
            return ResponseEntity.ok(new LoginResponse(token));

        } catch (AuthenticationException e) {
            // Invalid email or password
            return ResponseEntity
                    .status(401)
                    .body("{\"error\":\"Invalid email or password\"}");
        }
    }
}
