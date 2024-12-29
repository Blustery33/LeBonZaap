package org.example.back.controllers;

import org.example.back.annotations.CurrentUser;
import org.example.back.models.UserModel;
import org.example.back.models.dto.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public interface UserController {

    ResponseEntity<List<UserDTO>> getAllUser(@CurrentUser UserModel userId);
}
