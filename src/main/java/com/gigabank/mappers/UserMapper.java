package com.gigabank.mappers;

import com.gigabank.models.dto.request.user.CreateUserRequestDto;
import com.gigabank.models.dto.request.user.UpdateUserRequestDto;
import com.gigabank.models.dto.response.UserResponseDto;
import com.gigabank.models.entity.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Класс для преобразования между сущностями User и DTO для пользователей.
 * Использует MapStruct для автоматического преобразования объектов.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {
    /**
     * Преобразует DTO с данными для создания пользователя в сущность.
     *
     * @param createUserRequestDto DTO с данными пользователя
     * @return Сущность пользователя
     */
    User toEntity(CreateUserRequestDto createUserRequestDto);

    /**
     * Преобразует сущность пользователя в DTO.
     *
     * @param user Сущность пользователя
     * @return DTO пользователя
     */
    UserResponseDto toResponseDto(User user);

    /**
     * Обновляет сущность пользователя данными из DTO.
     * Игнорирует поля, которые равны null в DTO.
     *
     * @param dto    Данные для обновления
     * @param entity Сущность пользователя, которую нужно обновить
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UpdateUserRequestDto dto, @MappingTarget User entity);
}