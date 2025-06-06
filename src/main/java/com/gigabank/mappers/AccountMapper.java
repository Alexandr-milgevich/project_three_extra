package com.gigabank.mappers;

import com.gigabank.models.dto.request.account.CreateAccountRequestDto;
import com.gigabank.models.dto.request.account.AccountRequestDto;
import com.gigabank.models.dto.response.AccountResponseDto;
import com.gigabank.models.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "listTransactions", expression = "java(new ArrayList<>())")
    Account toEntity(CreateAccountRequestDto dto);

    /**
     * Преобразует сущность в DTO для ответа
     *
     * @param account сущность Account
     * @return DTO ответа
     */
    @Mapping(target = "id", ignore = true)
    AccountResponseDto toDto(Account account);

    /**
     * Преобразует сущность в DTO запроса
     *
     * @param dto DTO ответа
     * @return DTO запроса
     */
    @Mapping(target = "id", ignore = true)
    AccountRequestDto toRequestDto(AccountResponseDto dto);
}