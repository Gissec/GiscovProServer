package com.example.proserver.mappers;

import com.example.proserver.DTOs.request.RegisterUserRequest;
import com.example.proserver.DTOs.response.LoginUserResponse;
import com.example.proserver.DTOs.response.PublicUserResponse;
import com.example.proserver.models.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(ignore = true, target = "id")
    UserEntity userDtoToUserEntity(RegisterUserRequest userDto);

    @Mapping(ignore = true, target = "token")
    LoginUserResponse userEntityToLogin(UserEntity userEntity);

    PublicUserResponse userEntityToUser(UserEntity userEntity);
}

