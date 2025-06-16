package com.gigabank.mappers;

import com.gigabank.models.dto.request.account.AccountRequestDto;
import com.gigabank.models.dto.request.account.CreateAccountRequestDto;
import com.gigabank.models.dto.response.AccountResponseDto;
import com.gigabank.models.entity.BankAccount;
import org.mapstruct.Mapper;

/**
 * Класс для преобразования между сущностями Account и DTO для пользователей.
 * Использует MapStruct для автоматического преобразования объектов.
 */
@Mapper(componentModel = "spring")
public interface AccountMapper {
    /**
     * Преобразует DTO создания счета в сущность
     *
     * @param dto DTO создания счета
     * @return сущность Account
     */
    BankAccount toEntityFromCreateRequestDto(CreateAccountRequestDto dto);

    /**
     * Преобразует DTO создания счета в сущность
     *
     * @param dto DTO создания счета
     * @return сущность Account
     */
    BankAccount toEntityFromResponseDto(AccountResponseDto dto);

    /**
     * Преобразует сущность в DTO для ответа
     *
     * @param bankAccount сущность Account
     * @return DTO ответа
     */
    AccountResponseDto toResponseDto(BankAccount bankAccount);

    /**
     * Преобразует сущность в DTO запроса
     *
     * @param dto DTO ответа
     * @return DTO запроса
     */
    AccountRequestDto toRequestDto(AccountResponseDto dto);
}