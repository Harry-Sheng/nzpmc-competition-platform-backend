package com.nzpmc.CompetitionPlatform.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.security.Key;
import java.util.Objects;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    // Token validity in milliseconds
    @Value("${jwt.jwtExpirationMs}")
    private int jwtExpirationMs;

    // Generate JWT token
    public String createToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Get the cryptographic key used to sign JWTs.
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    // Extract all claims from the token
    public Claims extractAllClaims(String token) throws JwtException  {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isAdmin(String authorizationHeader){
        // Extract JWT token from Authorization header
        String token = extractToken(authorizationHeader);
        if (token == null) {
            throw new IllegalArgumentException("Authorization header missing or invalid");
        }

        // Validate and parse JWT token
        Claims claims = extractAllClaims(token);
        return "admin".equals(claims.get("role", String.class));
    }
}
