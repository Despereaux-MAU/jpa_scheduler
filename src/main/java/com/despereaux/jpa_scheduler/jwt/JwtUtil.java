package com.despereaux.jpa_scheduler.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final String secretKey;

    // application.properties에서 시크릿 키 주입
    public JwtUtil(@Value("${jwt.secret.key}") String secretKey) {
        this.secretKey = secretKey;
    }

    // JWT 생성 메서드
    public String generateToken(String email, String role) {
        Map<String, Object> claims = Map.of("role", role);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))  // 1시간 유효
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 토큰에서 Claims 추출
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 유효성 검증 메서드
    public boolean validateToken(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // 토큰에서 역할(Role) 추출 메서드
    public String getRoleFromToken(String token) {
        Claims claims = extractClaims(token);
        return claims.get("role", String.class);
    }

    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
}
