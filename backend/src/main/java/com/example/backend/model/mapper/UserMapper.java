package com.example.backend.model.mapper;

import com.example.backend.model.dto.UserDTO;
import com.example.backend.model.dto.UserRegisterDTO;
import com.example.backend.model.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "role.name", target = "role")
    UserDTO toDTO(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    User fromRegisterDTO(UserRegisterDTO dto);
}
