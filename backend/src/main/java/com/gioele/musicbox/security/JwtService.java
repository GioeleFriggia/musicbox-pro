package com.gioele.musicbox.security;

import com.gioele.musicbox.config.AppProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    private final AppProperties appProperties;

    public JwtService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String generateToken(String email) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + appProperties.getJwtExpirationMs());
        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(getSigningKey())
                .compact();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(java.util.Base64.getEncoder().encodeToString(appProperties.getJwtSecret().getBytes()));
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
