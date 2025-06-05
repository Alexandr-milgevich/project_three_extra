package com.gigabank.mappers;

import com.gigabank.models.dto.request.user.UserCreateRequestDto;
import com.gigabank.models.dto.request.user.UserRequestDto;
import com.gigabank.models.dto.request.user.UserUpdateRequestDto;
import com.gigabank.models.dto.response.UserResponseDto;
import com.gigabank.models.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Маппер для преобразования между сущностями User и DTO для пользователей.
 * Использует MapStruct для автоматического преобразования объектов.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Преобразует DTO с данными для создания пользователя в сущность.
     *
     * @param userRequestDto DTO с данными пользователя
     * @return Сущность пользователя
     */
    User toEntity(UserRequestDto userRequestDto);

    /**
     * Преобразует сущность пользователя в DTO.
     *
     * @param user Сущность пользователя
     * @return DTO пользователя
     */
    UserResponseDto toDto(User user);

    User toCreateEntity(UserCreateRequestDto userCreateRequestDto);


    /**
     * Обновляет сущность пользователя данными из DTO.
     * Игнорирует поля, которые равны null в DTO.
     *
     * @param dto Данные для обновления
     * @param entity Сущность пользователя, которую нужно обновить
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UserUpdateRequestDto dto, @MappingTarget User entity);
}


