package org.example.back.services.impl;

import lombok.Data;
import org.example.back.config.JwtService;
import org.example.back.config.RefreshTokenService;
import org.example.back.models.RefreshToken;
import org.example.back.models.UserModel;
import org.example.back.repository.RefreshTokenRepository;
import org.example.back.services.record.LoginResponse;
import org.example.back.services.record.RefreshTokenResponse;
import org.example.back.services.record.RegisterResponse;
import org.example.back.models.enums.UserType;
import org.example.back.models.mapper.UserMapper;
import org.example.back.repository.UserRepository;
import org.example.back.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
@Data
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;


    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @Value("${security.jwt.refresh-expiration-ms}")
    private long refreshExpiration;

    public AuthServiceImpl(JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper, RefreshTokenService refreshTokenService, RefreshTokenRepository refreshTokenRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public RegisterResponse signup(UserModel userModel) {
        String email = userModel.getEmail();

        // Verify if user already exist
        Optional<UserModel> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            // Response if email already exist
            return new RegisterResponse(false, "Email already exists: " + email, null);
        }

        String hashedPassword = passwordEncoder.encode(userModel.getPassword());

        // Create new User
        UserModel newUser = UserModel.builder()
                .email(email)
                .online(false)
                .created_at(LocalDateTime.now())
                .password(hashedPassword)
                .userType(UserType.CUSTOMER)
                .username(userModel.getUsername())
                .build();

        // Save User
        userRepository.save(newUser);

        return new RegisterResponse(true, "User registered successfully", userMapper.toDto(newUser));
    }


    @Override
    @Transactional
    public LoginResponse login(UserModel user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        //Get user from DB
        Optional<UserModel> existingUser = userRepository.findByEmail(user.getEmail());
        //if not exist error
        if (existingUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + user.getEmail());
        }

        // Get user and can generate Token
        UserModel authenticatedUser = existingUser.get();
        String token = jwtService.generateToken(user);
        if(authenticatedUser.getRefreshToken() != null){
            refreshToken(authenticatedUser.getRefreshToken().getToken());
            return new LoginResponse(userMapper.toDto(user),token,authenticatedUser.getRefreshToken().getToken());
        }

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(authenticatedUser);

        // Return LoginResponse with user DTO, access token and refresh token
        return new LoginResponse(userMapper.toDto(user),token,refreshToken.getToken());
    }


    @Override
    @Transactional
    public RefreshTokenResponse refreshToken(String refreshToken) {
        boolean result = refreshTokenService.validateRefreshToken(refreshToken);
        System.out.println(refreshToken);
        System.out.println(result);

        if(!result) {
            Long userId = refreshTokenService.extractUserIdFromToken(refreshToken);

            // get CurrentUser
            UserModel user = userRepository.findById(userId)
                    .orElseThrow(() -> new AuthenticationException("User not found") {});

            // Create new AccessToken
            String newAccessToken = jwtService.generateToken(user);

            // Return RefreshTokenResponse with, message , current refreshToken and new AccessToken
            return new RefreshTokenResponse("ok", refreshToken, newAccessToken);
        }else{
            // if the refreshToken is expired we delete him from DB
            refreshTokenRepository.findByToken(refreshToken)
                    .ifPresent(refreshTokenRepository::delete);

            // extract user id from refresh token
            Long userId = refreshTokenService.extractUserIdFromToken(refreshToken);

            // get User from DB
            UserModel user = userRepository.findById(userId)
                    .orElseThrow(() -> new AuthenticationException("User not found") {
                    });

            // Create a new Refresh Token
            String newRefreshToken = refreshTokenService.generateRefreshToken(user);
            // Create a new Access Token
            String newAccessToken = jwtService.generateToken(user);


            // Update refresh Token on DB
            RefreshToken existingRefreshToken = (RefreshToken) refreshTokenRepository.findByUser(user)
                    .orElseThrow(() -> new AuthenticationException("Refresh token not found"){});

            existingRefreshToken.setToken(newRefreshToken);
            existingRefreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpiration));
            refreshTokenRepository.save(existingRefreshToken);

            // Return  RefreshTokenResponse, message, NEW refresh token AND new AccessToken
            return new RefreshTokenResponse("newRefreshToken", newRefreshToken,newAccessToken);
            }


    }

}
