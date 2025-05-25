package com.gigabank.mappers;

import com.gigabank.models.dto.UserDto;
import com.gigabank.models.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}
