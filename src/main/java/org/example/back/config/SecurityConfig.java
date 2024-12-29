package org.example.back.config;

import org.example.back.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@Profile("!test")
public class SecurityConfig {

    @Autowired
    private  UserService userService;

    @Autowired
    private JwtFilter jwtFilter; // Injecting the custom JWT filter

    /**
     * Bean for password encoder using BCrypt algorithm.
     * Used for secure password hashing.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean to configure the security filter chain.
     * Defines HTTP security settings like authorization rules, session management, etc.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http
                // Disable CORS and CSRF for this configuration
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                // Define authorization rules
                .authorizeHttpRequests(authorize -> authorize
                        // Permit unauthenticated access to signup, login, and refresh token endpoints
                        .requestMatchers("/auth/signup", "/auth/login", "/auth/refreshToken","/swagger-ui/**",         // Swagger UI static resources
                                "/v3/api-docs/**",        // OpenAPI docs
                                "/swagger-ui.html").permitAll()
                        // Require authentication for all other endpoints
                        .anyRequest().authenticated()
                )
                // Set session management policy to stateless (no sessions created)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Bean for configuring an authentication provider.
     * Connects the DaoAuthenticationProvider to the user details service and sets the password encoder.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService); // Set custom UserService for loading user details
        provider.setPasswordEncoder(passwordEncoder()); // Set the password encoder
        return provider;
    }

    /**
     * Bean for authentication manager.
     * Provides authentication handling for the security context.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager(); // Retrieve and return the AuthenticationManager
    }

//    @Bean
//    public JwtFilter jwtFilter() {
//        return new JwtFilter();
//    }
}
