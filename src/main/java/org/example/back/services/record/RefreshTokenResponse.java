package org.example.back.services.record;


import org.example.back.models.RefreshToken;

import java.util.Optional;

public record RefreshTokenResponse(

        // what kind of message u can retrieve on reponse
        String message,
        // new or current refresh token
        String refreshToken,
        //new  accessToken
        String newAccessToken
) {
}
