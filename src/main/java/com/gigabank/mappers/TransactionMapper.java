package com.gigabank.mappers;

import com.gigabank.models.dto.request.transaction.CreateTransactionRequestDto;
import com.gigabank.models.dto.response.TransactionResponseDto;
import com.gigabank.models.entity.Transaction;
import org.mapstruct.Mapper;

/**
 * Класс для преобразования между сущностями Account и DTO для транзакций.
 * Использует MapStruct для автоматического преобразования объектов.
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper {
    /**
     * Преобразует DTO создания транзакции в сущность
     *
     * @param dto DTO создания счета
     * @return сущность Account
     */
    Transaction toEntityFromCreateRequestDto(CreateTransactionRequestDto dto);

    /**
     * Преобразует сущность в DTO для ответа
     *
     * @param transaction сущность Transaction
     * @return DTO ответа
     */
    TransactionResponseDto toResponseDto(Transaction transaction);
}