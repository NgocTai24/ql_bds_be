package vn.com.bds.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j; // Import Slf4j for logging
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j // Add for logging
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenBlacklistService tokenBlacklistService; // <-- INJECT BLACKLIST SERVICE

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        try {
            userEmail = jwtService.extractUsername(jwt);

            // If email exists and user is not yet authenticated in this request's context
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // --- ADD BLACKLIST CHECK ---
                boolean isTokenValid = jwtService.isTokenValid(jwt, userDetails);
                // Check blacklist *after* basic validation but *before* authentication
                boolean isTokenBlacklisted = tokenBlacklistService.isBlacklisted(jwt);

                if (isTokenValid && !isTokenBlacklisted) {
                    // If valid and NOT blacklisted, set authentication
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Authenticated user: {}", userEmail); // Optional log
                } else if (isTokenBlacklisted) {
                    log.warn("Attempted use of blacklisted JWT for user: {}", userEmail);
                    // Do not set authentication, request will likely be denied later by security rules
                }
                // If !isTokenValid, do nothing, request will fail later
                // --- END BLACKLIST CHECK ---
            }
        } catch (Exception e) {
            // Handle potential errors during token extraction/validation (e.g., expired, malformed)
            log.warn("JWT processing error: {}", e.getMessage());
            // Optionally, you might want to clear the security context here if partially set
            // SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response); // Continue the filter chain
    }
}