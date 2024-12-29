package org.example.back.models.mapper;

import org.example.back.models.UserModel;
import org.example.back.models.WorkersModel;
import org.example.back.models.dto.UserDTO;
import org.example.back.models.enums.UserType;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends GenericMapperImpl<UserModel, UserDTO> {
    public UserMapper() {
        super(UserModel.class, UserDTO.class);
    }
}
