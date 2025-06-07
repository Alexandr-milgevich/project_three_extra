package com.gigabank.utility.converters;

import com.gigabank.constants.status.AccountStatus;
import com.gigabank.exceptions.account.AccountValidationException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

/**
 * Конвертер для преобразования {@link AccountStatus}
 * в строковое представление для базы данных и обратно.
 */
@Converter
public class AccountStatusConverter implements AttributeConverter<AccountStatus, String> {
    /**
     * Преобразует значение {@link AccountStatus} в строку для сохранения в базу данных.
     *
     * @param status статус счета
     * @return строковое представление статуса для базы данных ("Active", "Blocked", "Archived")
     */
    @Override
    public String convertToDatabaseColumn(AccountStatus status) {
        if (Objects.isNull(status)) throw new AccountValidationException("Статус не может быть пустым");
        return switch (status) {
            case ACTIVE -> "Active";
            case BLOCKED -> "Blocked";
            case ARCHIVED -> "Archived";
        };
    }

    /**
     * Преобразует строковое значение из базы данных в соответствующий {@link AccountStatus}.
     * Если значение неизвестно или null, выбрасывает {@link AccountValidationException}.
     *
     * @param dbData строковое значение статуса из базы данных
     * @return соответствующий статус счета {@link AccountStatus}
     */
    @Override
    public AccountStatus convertToEntityAttribute(String dbData) {
        if (Objects.isNull(dbData)) throw new AccountValidationException("Статус не может быть пустым");
        return switch (dbData) {
            case "Active" -> AccountStatus.ACTIVE;
            case "Blocked" -> AccountStatus.BLOCKED;
            case "Archived" -> AccountStatus.ARCHIVED;
            default -> throw new AccountValidationException("Неизвестный статус: " + dbData);
        };
    }
}