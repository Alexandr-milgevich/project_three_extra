package com.gigabank.mappers;

import com.gigabank.models.dto.AccountDto;
import com.gigabank.models.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    //todo СДЕЛАЙ описание класса!

    @Mapping(target = "id", ignore = true)
    AccountDto toDto(Account account);

    Account toEntity(AccountDto accountDto);
}

