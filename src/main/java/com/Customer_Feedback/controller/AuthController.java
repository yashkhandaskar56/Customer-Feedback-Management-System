
package com.Customer_Feedback.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Customer_Feedback.entity.AppUser;
import com.Customer_Feedback.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> request) {

        try {
            String name = request.get("name");
            String email = request.get("email");
            String password = request.get("password");

            if (name == null || name.isBlank()
                    || email == null || email.isBlank()
                    || password == null || password.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Name, email and password are required"));
            }

            AppUser user = authService.register(name, email, password);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "message", "Registration successful",
                            "userId", user.getId(),
                            "name", user.getName(),
                            "email", user.getEmail()
                    ));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {

        try {
            String email = request.get("email");
            String password = request.get("password");

            if (email == null || email.isBlank()
                    || password == null || password.isBlank()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "Email and password are required"));
            }

            String token = authService.login(email, password);

            return ResponseEntity.ok(Map.of(
                    "message", "Login successful",
                    "token", token,
                    "tokenType", "Bearer"
            ));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", ex.getMessage()));
        }
    }
}