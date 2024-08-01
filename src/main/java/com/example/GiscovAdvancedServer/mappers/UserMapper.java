package com.example.GiscovAdvancedServer.mappers;

import com.example.GiscovAdvancedServer.DTOs.request.RegisterUserRequest;
import com.example.GiscovAdvancedServer.DTOs.response.LoginUserResponse;
import com.example.GiscovAdvancedServer.DTOs.response.PublicUserResponse;
import com.example.GiscovAdvancedServer.DTOs.response.PutUserResponse;
import com.example.GiscovAdvancedServer.models.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(ignore = true, target = "id")
    UserEntity userDtoToUserEntity(RegisterUserRequest userDto);

    @Mapping(ignore = true, target = "token")
    LoginUserResponse userEntityToLogin(UserEntity userEntity);

    PublicUserResponse userEntityToUser(UserEntity userEntity);

    PutUserResponse userEntityToPutUserResponse(UserEntity userEntity);
}

