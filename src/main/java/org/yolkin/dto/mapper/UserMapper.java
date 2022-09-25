package org.yolkin.dto.mapper;

import org.yolkin.dto.UserCreationDTO;
import org.yolkin.dto.UserDTO;
import org.yolkin.model.UserEntity;

public class UserMapper {
    public static UserEntity toUser(UserCreationDTO userCreationDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(userCreationDTO.getName());
        return userEntity;
    }

    public static UserDTO toUserDto(UserEntity userEntity) {
        return new UserDTO(userEntity.getId(), userEntity.getName(), userEntity.getEvents());
    }

    public static UserEntity toUser(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(userDTO.getId());
        userEntity.setName(userDTO.getName());
        return userEntity;
    }
}
