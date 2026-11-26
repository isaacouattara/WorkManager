package com.workmanager.infrastructure.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// Lit "Authorization: Bearer <token>", valide le JWT et place l'utilisateur dans le contexte de sécurité.
// Volontairement PAS un @Component : il est ajouté à la chaîne dans SecurityConfig.
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            try {
                String userId = jwtService.extractUserId(header.substring(7));
                // Principal = id de l'utilisateur ; pas encore de rôles global
                var auth = UsernamePasswordAuthenticationToken.authenticated(userId, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (JwtException | IllegalArgumentException e) {
                // Token invalide ou expiré : on ne authentifie pas, la route protégée répondra 401
                SecurityContextHolder.clearContext();
            }
        }

        chain.doFilter(request, response);
    }
}