package org.example.back.controllers.impl;

import org.example.back.controllers.UserController;
import org.example.back.models.UserModel;
import org.example.back.models.dto.UserDTO;
import org.example.back.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserControllerImpl implements UserController {


    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUser(UserModel user) {
        try {
            var result = userService.getAllUser();
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}