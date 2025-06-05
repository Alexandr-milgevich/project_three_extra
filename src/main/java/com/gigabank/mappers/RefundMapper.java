package com.gigabank.mappers;

import com.gigabank.models.dto.RefundDto;
import com.gigabank.models.entity.Refund;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RefundMapper {
    //todo СДЕЛАЙ описание класса!

    @Mapping(target = "id", ignore = true)
    RefundDto toDto(Refund refund);

    Refund toEntity(RefundDto refundDto);
}