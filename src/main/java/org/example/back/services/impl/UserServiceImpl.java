package org.example.back.services.impl;

import org.example.back.models.UserModel;
import org.example.back.models.dto.UserDTO;
import org.example.back.models.mapper.UserMapper;
import org.example.back.repository.UserRepository;
import org.example.back.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UserDTO> getAllUser() {
        // get all users , convert entity to data transfert object for front , return as list.
        return userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Loads a user from the database using their email address.
     * This method is required by the UserDetailsService interface for integrating
     * Spring Security with a custom user repository.
     *
     * @param email the email address of the user to retrieve
     * @return the UserModel object who extend UserDetails form Spring Security representing the user, if found
     * @throws UsernameNotFoundException if no user with the specified email is found
     *
     * Notes:
     * - This method assumes that the email is unique in the database.
     * - Throws a custom message in case the user does not exist to help with debugging.
     */

    @Override
    public UserModel loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

}