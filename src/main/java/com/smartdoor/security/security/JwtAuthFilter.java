package com.smartdoor.security.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Runs once per request: extracts the Bearer token, validates it, and — if valid —
 * populates the SecurityContext so downstream controllers can rely on @PreAuthorize / principal.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        System.out.println("========== JWT FILTER ==========");
System.out.println("Header: " + header);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (jwtTokenProvider.validateToken(token)) {

    System.out.println("Token VALID");

    String username = jwtTokenProvider.getUsernameFromToken(token);

    System.out.println("Username = " + username);

    UserDetails userDetails =
            userDetailsService.loadUserByUsername(username);

    System.out.println("User Loaded = " + userDetails.getUsername());

    UsernamePasswordAuthenticationToken authToken =
            new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities());

    SecurityContextHolder.getContext().setAuthentication(authToken);

    System.out.println("Authentication Set");
}
else{
    System.out.println("INVALID TOKEN");
}
        }

        filterChain.doFilter(request, response);
    }
}
