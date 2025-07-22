package org.example.geoback.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "Jd0BqE6Gmqpy8Eut9yhuocsDWOmzo5RP";

    // 🔹 Rol bilgisi içeren token üretimi
    public static String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 saat
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // 🔹 Token'dan kullanıcı adını al
    public static String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    // 🔹 Token'dan rol al
    public static String getRoleFromToken(String token) {
        return getAllClaimsFromToken(token).get("roles", String.class);
    }

    // 🔹 Token geçerli mi?
    public static boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public static boolean validateToken(String token) {
        try {
            getAllClaimsFromToken(token); // Hata atarsa geçersizdir
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    // 🔹 Token'ın sona erme tarihini al
    public static Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    // 🔹 Tüm claim'leri al (özel bilgiler burada)
    private static Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
