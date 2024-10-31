package com.despereaux.jpa_scheduler.security;

import com.despereaux.jpa_scheduler.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, password);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        String username = authResult.getName();
        List<String> roles = authResult.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String role = roles.isEmpty() ? "USER" : roles.get(0);
        String token = jwtUtil.generateToken(username, role);
        response.addHeader("Authorization", "Bearer " + token);
    }
}
