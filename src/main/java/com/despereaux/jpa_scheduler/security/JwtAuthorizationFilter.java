package com.despereaux.jpa_scheduler.security;

import com.despereaux.jpa_scheduler.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthorizationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.resolveToken(request);

        try {
            if (token != null && jwtUtil.validateToken(token)) {
                Authentication auth = jwtUtil.getAuthentication(token);

                if (auth instanceof AbstractAuthenticationToken authenticationToken) {
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                }

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
