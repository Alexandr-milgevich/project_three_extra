package com.gigabank.mappers;

import com.gigabank.models.dto.request.account.CreateBankAccountRequestDto;
import com.gigabank.models.dto.response.BankAccountResponseDto;
import com.gigabank.models.entity.BankAccount;
import org.mapstruct.Mapper;

/**
 * Класс для преобразования между сущностями Account и DTO для пользователей.
 * Использует MapStruct для автоматического преобразования объектов.
 */
@Mapper(componentModel = "spring")
public interface BankAccountMapper {
    /**
     * Преобразует DTO создания счета в сущность
     *
     * @param dto DTO создания счета
     * @return сущность Account
     */
    BankAccount toEntityFromCreateRequestDto(CreateBankAccountRequestDto dto);

    /**
     * Преобразует сущность в DTO для ответа
     *
     * @param bankAccount сущность Account
     * @return DTO ответа
     */
    BankAccountResponseDto toResponseDto(BankAccount bankAccount);
}