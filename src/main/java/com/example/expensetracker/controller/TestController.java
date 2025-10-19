package com.example.expensetracker.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@CrossOrigin(origins = "*")
public class TestController {

    @GetMapping
    public String testProtectedRoute(HttpServletRequest request) {
        // This value was set in JwtFilter when token was valid
        String userId = (String) request.getAttribute("userId");

        if (userId == null) {
            return "No valid token provided!";
        }

        return "Access granted ✅ | User ID: " + userId;
    }
}
