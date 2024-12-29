package org.example.back.controllers.impl;

import org.example.back.controllers.AuthController;
import org.example.back.models.RefreshToken;
import org.example.back.models.UserModel;
import org.example.back.services.record.LoginResponse;
import org.example.back.services.record.RefreshTokenResponse;
import org.example.back.services.record.RegisterResponse;
import org.example.back.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthControllerImpl implements AuthController {

    @Autowired
    private AuthService authService;

    @Override
    @PostMapping("/signup")
    public ResponseEntity<RegisterResponse> signup(UserModel user) {
       var result = authService.signup(user);
        return ResponseEntity.ok(result);
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(UserModel user) {
        LoginResponse result = authService.login(user);
        return ResponseEntity.ok(result);
    }

    @Override
    @PostMapping("/refreshToken")
    public ResponseEntity<RefreshTokenResponse> refreshToken(RefreshToken refreshToken) {
        RefreshTokenResponse result = authService.refreshToken(refreshToken.getToken());
        return ResponseEntity.ok(result);
    }
}
