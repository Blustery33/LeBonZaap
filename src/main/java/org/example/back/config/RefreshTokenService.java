package org.example.back.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.example.back.models.RefreshToken;
import org.example.back.models.UserModel;
import org.example.back.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@Data
public class RefreshTokenService {

    private final JwtService jwtService;
    //test
    @Value("${security.jwt.refresh-expiration-ms}")
    private long refreshExpiration;

    @Value("${hs256.secret.keys}")
    private String secretKeys;

    private final RefreshTokenRepository refreshTokenRepository;




    @Transactional
    public RefreshToken createRefreshToken(UserModel user) {

        // Create new refresh token
        RefreshToken refreshToken = new RefreshToken();
        // Call method for generate the token
        refreshToken.setToken(generateRefreshToken(user));
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpiration));
        refreshToken.setUser(user);
        return refreshTokenRepository.save(refreshToken);
    }

    public boolean validateRefreshToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);

        // Return true if the token is expired
        return refreshToken
                .map(rt -> jwtService.isTokenExpired(rt.getToken()))
                .orElse(false);
    }


    public Long extractUserIdFromToken(String token) {
        System.out.println("Extracting userId from token: " + token);  // Debug
        Claims claims = Jwts.parser()
                .verifyWith(jwtService.getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        // Extract user id from the claim
        return claims.get("userId", Long.class);
    }
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteById(userId);
    }

    public String generateRefreshToken(UserModel user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getEmail()); // Subject:  user email
        claims.put("userId", user.getId()); // user id, we can add more info if we want

        // Expiration Date
        Date expirationDate = new Date(System.currentTimeMillis() + refreshExpiration);

        // Refresh token generate from jwt
        return Jwts.builder()
                .claims(claims)  // Adds extra claims (e.g., roles, permissions)// Sets the "subject" (user's email)
                .issuedAt(new Date(System.currentTimeMillis()))  // Sets the issued date as the current time
                .expiration(expirationDate)  // Sets expiration time
                .signWith(jwtService.getSigningKey())  // Signs the token with the signing key
                .compact();

    }
   /* public String generateRefreshTokenFAKE(UserModel user) {
        // Création des claims JWT (données supplémentaires incluses dans le jeton)
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", user.getEmail()); // Subject: email de l'utilisateur
        claims.put("userId", user.getId()); // ID de l'utilisateur (vous pouvez ajouter d'autres informations si nécessaire)

        // Calcul de la date d'expiration
        Date expirationDate = new Date(System.currentTimeMillis() + refreshExpiration);

        // Génération du refresh token JWT
        return Jwts.builder()
                .claims(claims)  // Adds extra claims (e.g., roles, permissions)// Sets the "subject" (user's email)
                .issuedAt(new Date(System.currentTimeMillis()))  // Sets the issued date as the current time
                .expiration(new Date(System.currentTimeMillis() - 1000))  // Sets expiration time
                .signWith(jwtService.getSigningKey())  // Signs the token with the signing key
                .compact();

    }*/
}