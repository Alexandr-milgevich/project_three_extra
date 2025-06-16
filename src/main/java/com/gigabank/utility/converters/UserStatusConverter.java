package com.gigabank.utility.converters;

import com.gigabank.constants.status.UserStatus;
import com.gigabank.exceptions.buisnes_logic.EntityValidationException;
import com.gigabank.models.entity.User;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;

/**
 * Конвертер для преобразования {@link UserStatus}
 * в строковое представление для базы данных и обратно.
 */
@Converter
public class UserStatusConverter implements AttributeConverter<UserStatus, String> {
    /**
     * Преобразует значение {@link UserStatus} в строку для сохранения в базу данных.
     *
     * @param status статус пользователя
     * @return строковое представление статуса для базы данных ("Active", "Blocked", "Deleted")
     */
    @Override
    public String convertToDatabaseColumn(UserStatus status) {
        if (Objects.isNull(status))
            throw new EntityValidationException(User.class, "Статус не может быть пустым");
        return switch (status) {
            case ACTIVE -> "Active";
            case BLOCKED -> "Blocked";
            case DELETED -> "Deleted";
        };
    }

    /**
     * Преобразует строковое значение из базы данных в соответствующий {@link UserStatus}.
     * Если значение неизвестно, выбрасывает {@link EntityValidationException}.
     *
     * @param dbData строковое значение статуса из базы данных
     * @return соответствующий статус пользователя {@link UserStatus}
     */
    @Override
    public UserStatus convertToEntityAttribute(String dbData) {
        if (Objects.isNull(dbData))
            throw new EntityValidationException(User.class, "Статус не может быть пустым");
        return switch (dbData) {
            case "Active" -> UserStatus.ACTIVE;
            case "Blocked" -> UserStatus.BLOCKED;
            case "Deleted" -> UserStatus.DELETED;
            default -> throw new EntityValidationException(User.class, "Неизвестный статус: " + dbData);
        };
    }
}