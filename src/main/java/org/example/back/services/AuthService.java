package org.example.back.services;

import org.example.back.models.RefreshToken;
import org.example.back.models.UserModel;
import org.example.back.services.record.LoginResponse;
import org.example.back.services.record.RefreshTokenResponse;
import org.example.back.services.record.RegisterResponse;

import java.util.Optional;

public interface AuthService {

     RegisterResponse signup(UserModel userModel);
     LoginResponse login(UserModel userModel);
     RefreshTokenResponse refreshToken(String refreshToken);
}
