package org.example.back.services;

import org.apache.catalina.User;
import org.example.back.models.UserModel;
import org.example.back.models.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService extends UserDetailsService {

    List<UserDTO> getAllUser();

}
