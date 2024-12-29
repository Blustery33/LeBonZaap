package org.example.back.services.record;

import org.example.back.models.dto.UserDTO;

public record RegisterResponse(boolean success, String message, UserDTO user) {}

