package org.example.geoback.controller;

import org.example.geoback.entity.User;
import org.example.geoback.repository.UserRepository;
import org.example.geoback.util.HashUtil;
import org.example.geoback.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Kullanıcı adı zaten var.");
        }

        String hashedPassword = HashUtil.md5(user.getPassword());
        user.setPassword(hashedPassword);
        user.setRole("VERİ_OKUYUCU");
        userRepository.save(user);

        return ResponseEntity.ok("Kayıt başarılı");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        User dbUser = userRepository.findByUsername(user.getUsername());

        if (dbUser == null) {
            return ResponseEntity.status(401).body("Kullanıcı bulunamadı.");
        }

        String hashedPassword = HashUtil.md5(user.getPassword());

        if (!dbUser.getPassword().equals(hashedPassword)) {
            return ResponseEntity.status(401).body("Şifre hatalı.");
        }

        String token = JwtUtil.generateToken(user.getUsername(), user.getRole());

        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/authkey-check")
    public ResponseEntity<String> authkeyCheck(@RequestParam String authkey) {
        String token = authkey;
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        String username;
        try {
            username = JwtUtil.getUsernameFromToken(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Geçersiz token");
        }

        if (username == null || username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Kullanıcı bulunamadı");
        }

        return ResponseEntity.ok(username);
    }


}
