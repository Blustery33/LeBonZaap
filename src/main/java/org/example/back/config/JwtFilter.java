package org.example.back.config;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.back.models.UserModel;
import org.springframework.context.ApplicationContext;
import org.example.back.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    ApplicationContext context;

    /**
     * This method filters incoming HTTP requests to validate JWT tokens.
     * It is executed once per request.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Retrieve the request path for debugging purposes
        String path = request.getServletPath();
        System.out.println("Request Path: " + path);

        // Skip JWT validation for the login and signup endpoints
        if (path.equals("/auth/signup") || path.equals("/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the Authorization header from the request
        String authHeader = request.getHeader("Authorization");
        String jwtToken = null;
        String email = null;

        // Check if the Authorization header contains a Bearer token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7); // Remove the "Bearer " prefix
            try {
                // Extract the email address from the JWT token
                email = jwtService.extractEmail(jwtToken);
            } catch (ExpiredJwtException e) {
                // Handle the case where the token is expired
                response.setHeader("Token-Expired", "true");
                filterChain.doFilter(request, response);
                return;
            }
        }

        // Validate the JWT token and set the security context if valid
        if (jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load the user details from the database using the email extracted from the token
            UserModel userDetails = (UserModel) context.getBean(UserService.class).loadUserByUsername(email);
            // Validate the token against the user details
            if (jwtService.validateToken(jwtToken, userDetails)) {
                // Create an authentication token and set it in the security context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue the filter chain for the request
        filterChain.doFilter(request, response);
    }
}
