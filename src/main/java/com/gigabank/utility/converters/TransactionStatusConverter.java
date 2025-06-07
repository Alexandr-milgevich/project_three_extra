package com.gigabank.utility.converters;

import com.gigabank.constants.status.TransactionStatus;
import com.gigabank.exceptions.transaction.TransactionValidationException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

/**
 * Конвертер для преобразования {@link TransactionStatus}
 * в строковое представление для базы данных и обратно.
 */
@Converter
public class TransactionStatusConverter implements AttributeConverter<TransactionStatus, String> {
    /**
     * Преобразует значение {@link TransactionStatus} в строку для сохранения в базу данных.
     *
     * @param status статус транзакции
     * @return строковое представление статуса для базы данных ("Active", "Blocked", "Archived")
     */
    @Override
    public String convertToDatabaseColumn(TransactionStatus status) {
        if (Objects.isNull(status)) throw new TransactionValidationException("Статус не может быть пустым");
        return switch (status) {
            case ACTIVE -> "Active";
            case BLOCKED -> "Blocked";
            case ARCHIVED -> "Archived";
        };
    }

    /**
     * Преобразует строковое значение из базы данных в соответствующий {@link TransactionStatus}.
     * Если значение неизвестно, выбрасывает {@link TransactionValidationException}.
     *
     * @param dbData строковое значение статуса из базы данных
     * @return соответствующий статус транзакции {@link TransactionStatus}
     */
    @Override
    public TransactionStatus convertToEntityAttribute(String dbData) {
        if (Objects.isNull(dbData)) throw new TransactionValidationException("Статус не может быть пустым");
        return switch (dbData) {
            case "Active" -> TransactionStatus.ACTIVE;
            case "Blocked" -> TransactionStatus.BLOCKED;
            case "Archived" -> TransactionStatus.ARCHIVED;
            default -> throw new TransactionValidationException("Неизвестный статус: " + dbData);
        };
    }
}