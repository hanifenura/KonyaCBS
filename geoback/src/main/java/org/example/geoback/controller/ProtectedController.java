package org.example.geoback.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protected")

public class ProtectedController {
    @GetMapping("/hello")
    public String protectedHello() {
        return "JWT ile giriş yaptın, hoş geldin!";
    }
}
