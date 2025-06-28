package com.gigabank.utility.converters;

import com.gigabank.constants.status.BankAccountStatus;
import com.gigabank.exceptions.buisnes_logic.EntityValidationException;
import com.gigabank.models.entity.BankAccount;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

/**
 * Конвертер для преобразования {@link BankAccountStatus}
 * в строковое представление для базы данных и обратно.
 */
@Converter
public class BankAccountStatusConverter implements AttributeConverter<BankAccountStatus, String> {
    /**
     * Преобразует значение {@link BankAccountStatus} в строку для сохранения в базу данных.
     *
     * @param status статус счета
     * @return строковое представление статуса для базы данных ("Active", "Blocked", "Archived")
     */
    @Override
    public String convertToDatabaseColumn(BankAccountStatus status) {
        if (Objects.isNull(status))
            throw new EntityValidationException(BankAccount.class, "Статус не может быть пустым");
        return switch (status) {
            case ACTIVE -> "Active";
            case BLOCKED -> "Blocked";
            case ARCHIVED -> "Archived";
        };
    }

    /**
     * Преобразует строковое значение из базы данных в соответствующий {@link BankAccountStatus}.
     * Если значение неизвестно или null, выбрасывает {@link EntityValidationException}.
     *
     * @param dbData строковое значение статуса из базы данных
     * @return соответствующий статус счета {@link BankAccountStatus}
     */
    @Override
    public BankAccountStatus convertToEntityAttribute(String dbData) {
        if (Objects.isNull(dbData))
            throw new EntityValidationException(BankAccount.class, "Статус не может быть пустым");
        return switch (dbData) {
            case "Active" -> BankAccountStatus.ACTIVE;
            case "Blocked" -> BankAccountStatus.BLOCKED;
            case "Archived" -> BankAccountStatus.ARCHIVED;
            default -> throw new EntityValidationException(BankAccount.class, "Неизвестный статус: " + dbData);
        };
    }
}