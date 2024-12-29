package org.example.back.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.back.models.UserModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${hs256.secret.keys}")
    private String secretKeys;  // Secret key for signing JWT, retrieved from application properties

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;  // JWT expiration time in milliseconds, retrieved from application properties

    // Extracts the email from the JWT token by getting the "subject" claim Subject = User who ask
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extracts a specific claim from the JWT token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);  // Extract all claims from the token
        return claimsResolver.apply(claims);  // Resolves and returns the required claim
    }

    // Extracts all claims from the JWT token using the signing key
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())  // Sets the signing key for verification
                .build()
                .parseSignedClaims(token)  // Parses the JWT and returns the claims body
                .getPayload();
    }

    // Extracts the expiration date from the token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Returns the JWT expiration time
    public long getJwtExpiration() {
        return jwtExpiration;
    }

    // Generates a JWT token for a given user (without additional claims)
    public String generateToken(UserModel user) {
        return generateToken(new HashMap<>(), user);  // Calls overloaded method with empty extra claims
    }

    // Generates a JWT token with additional claims for a given user
    public String generateToken(Map<String, Object> extraClaims, UserModel user) {
        return buildToken(extraClaims, user, jwtExpiration);  // Passes expiration time to token builder
    }

    // Builds the JWT token with additional claims, user information, and expiration time
    public String buildToken(Map<String, Object> extraClaims, UserModel user, long expirationTime) {
        return Jwts.builder()
                .claims(extraClaims)  // Adds extra claims (e.g., roles, permissions)
                .subject(user.getEmail())  // Sets the "subject" (user's email)
                .issuedAt(new Date(System.currentTimeMillis()))  // Sets the issued date as the current time
                .expiration(new Date(System.currentTimeMillis() + expirationTime))  // Sets expiration time
                .signWith(getSigningKey())  // Signs the token with the signing key
                .compact();  // Returns the JWT as a string
    }

    // Validates the JWT token by comparing the email in the token and the provided user, and checking expiration
    public Boolean validateToken(String token, UserModel user) {
        final String email = extractEmail(token);  // Extracts the email from the token
        try {
            return email.equals(user.getEmail()) && !isTokenExpired(token);  // Validates email and expiration
        } catch (ExpiredJwtException e) {
            // Handles token expiration explicitly (returns false)
            return false;
        }
    }

    // Checks if the JWT token has expired
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());  // Compares expiration date with current time
    }

    // Returns the signing key derived from the secret key
    public SecretKey getSigningKey() {
        if (secretKeys == null || secretKeys.isEmpty()) {  // Checks if the secret key is not null or empty
            throw new IllegalArgumentException("Secret key is not configured.");  // Throws an error if the key is missing
        }
        byte[] keyBytes = secretKeys.getBytes();  // Converts the secret key to bytes
        return Keys.hmacShaKeyFor(keyBytes);  // Returns the HMAC signing key
    }
}
