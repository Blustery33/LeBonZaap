package org.example.back.services.record;

import io.swagger.v3.oas.annotations.media.Schema;
import org.example.back.models.RefreshToken;
import org.example.back.models.dto.UserDTO;

public record LoginResponse(

        @Schema(description = "user data")
        UserDTO user,
        @Schema(description = "JWT token")
        String token,
        @Schema(description = "JWT refreshToken")
        String  refreshToken

) {
}
