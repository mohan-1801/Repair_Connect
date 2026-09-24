package com.repairconnect.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
public class RootController {
    @GetMapping({"/", "/api", "/api/"})
    public Map<String, Object> root() {
        return Map.of(
            "app", "RepairConnect API",
            "status", "running",
            "version", "1.0.0",
            "endpoints", Map.of(
                "login",     "POST /api/auth/login",
                "register",  "POST /api/auth/register/customer | /api/auth/register/provider",
                "providers", "GET  /api/providers",
                "categories","GET  /api/categories",
                "bookings",  "GET  /api/bookings/my  (Customer) | /api/bookings/provider (Provider)",
                "admin",     "GET  /api/admin/dashboard"
            ),
            "frontend", "http://localhost:5173"
        );
    }
}
