package com.gigabank.mappers;

import com.gigabank.models.dto.response.TransactionResponseDto;
import com.gigabank.models.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    //todo СДЕЛАЙ описание класса!

    @Mapping(target = "id", ignore = true)
    TransactionResponseDto toDto(Transaction transaction);

    Transaction toEntity(TransactionResponseDto transactionResponseDto);
}