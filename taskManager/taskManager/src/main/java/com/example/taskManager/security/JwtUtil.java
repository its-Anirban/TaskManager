package com.example.taskManager.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // Make sure this secret is at least 32 bytes long
    private final SecretKey key = Keys.hmacShaKeyFor("ThisIsAVeryStrongSecretKeyForJWT123456".getBytes());
    private final long expirationMs = 3600000L;
    private final String issuer = "TaskManagerAPI";

    public String generateToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .setSubject(username)
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return issuer.equals(claims.getBody().getIssuer());
        } catch (JwtException | IllegalArgumentException ex) {
            // log and return false
            System.out.println("JWT validation failed: " + ex.getMessage());
            return false;
        }
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
