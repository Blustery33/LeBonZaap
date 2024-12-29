package org.example.back.controllers;

import jakarta.validation.Valid;
import org.example.back.models.RefreshToken;
import org.example.back.models.UserModel;
import org.example.back.services.record.LoginResponse;
import org.example.back.services.record.RefreshTokenResponse;
import org.example.back.services.record.RegisterResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthController {

    ResponseEntity<RegisterResponse> signup(@Valid @RequestBody UserModel user);
    ResponseEntity<LoginResponse> login(@Valid @RequestBody UserModel user);
    ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody RefreshToken refreshToken);
}
