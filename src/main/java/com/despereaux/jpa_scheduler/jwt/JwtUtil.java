package com.despereaux.jpa_scheduler.jwt;

import com.despereaux.jpa_scheduler.exception.TokenExpiredException;
import com.despereaux.jpa_scheduler.exception.TokenMissingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKeyString;
    private SecretKey secretKey;

    @PostConstruct
    protected void init() {
        String encodedKey = Base64.getEncoder().encodeToString(secretKeyString.getBytes());
        this.secretKey = Keys.hmacShaKeyFor(encodedKey.getBytes());
    }

    public String generateToken(String username, String role) {

        Claims claims = Jwts.claims();
        claims.put("username", username);
        claims.put("role", role);

        long validityInMilliseconds = 3600000;
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("토큰이 만료되었습니다.");
        } catch (Exception e) {
            return false;
        }
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("username", String.class);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        String email = claims.getSubject();
        List<String> roles = claims.get("roles", List.class);

        return new UsernamePasswordAuthenticationToken(
                email, null, roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
        );
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new TokenMissingException("토큰이 필요합니다.");
        }
        return bearerToken.substring(7);
    }
}
