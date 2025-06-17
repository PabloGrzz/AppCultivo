package com.example.BackEndApiPlantas.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;



import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    @Value("${jwt.secret:bXlTdXBlclNlY3JldEtleTEyMzQ1Njc4OTBBQkNERUY=}")
    private String secretKey;
    
    @Value("${jwt.expiration:86400000}") // 24 horas por defecto
    private long expirationTime;

    public String generateToken(String username) {
        System.out.println("WARNING: Token generado sin rol para usuario: " + username);
        return generateToken(username, null);
    }
    
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        
        // Añadir el rol como un claim en el token si está disponible
        if (role != null) {
            claims.put("role", role);
            System.out.println("INFO: Token generado con rol: " + role + " para usuario: " + username);
        } else {
            System.out.println("WARNING: No se proporcionó rol para usuario: " + username);
        }
        
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getKey())
                .compact();
    }

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    // Método para extraer el rol del token
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}