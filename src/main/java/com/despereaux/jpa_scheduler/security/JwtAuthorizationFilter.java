package com.despereaux.jpa_scheduler.security;

import com.despereaux.jpa_scheduler.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthorizationFilter(String defaultFilterProcessesUrl, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        super(defaultFilterProcessesUrl);
        this.jwtUtil = jwtUtil;
        setAuthenticationManager(authenticationManager);  // AuthenticationManager 설정
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            return null;  // JWT가 없는 경우 null 반환
        }

        token = token.substring(7);  // "Bearer " 이후의 토큰만 추출
        Claims claims = jwtUtil.extractClaims(token);

        if (claims == null) {
            return null;  // JWT가 유효하지 않은 경우 null 반환
        }

        String username = claims.getSubject();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, null);

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}
