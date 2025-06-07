package com.gigabank.utility.validators;

import com.gigabank.constants.TransactionType;
import com.gigabank.constants.status.TransactionStatus;
import com.gigabank.exceptions.transaction.TransactionValidationException;
import com.gigabank.models.entity.Transaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static com.gigabank.constants.TransactionCategories.VALID_TRANSACTION_CATEGORIES;
import static com.gigabank.constants.TransactionType.SUPPORTED_TYPES;

/**
 * Сервис для валидации данных транзакции перед сохранением в базу данных.
 * При обнаружении ошибок выбрасывает соответствующие исключения.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ValidateTransactionService {


    public void validateUnderSave(Transaction transaction) {
        log.info("Начало валидации транзакции перед сохранением.");

        checkId(transaction.getId());
        checkAmount(transaction.getAmount());
        checkType(transaction.getType());
        checkCategory(transaction.getCategory());

        log.info("Завершение валидации транзакции перед сохранением.");
    }

    /**
     * Проверяет два статуса транзакции: старый и изменяемый.
     *
     * @param newStatus статус для изменения
     * @param oldStatus первоначальный статус
     */
    public void checkTransactionStatus(TransactionStatus newStatus, TransactionStatus oldStatus) {
        if (TransactionStatus.isValid(newStatus.name()) || oldStatus == newStatus)
            throw new TransactionValidationException("Недопустимый статус. Новый статус: "
                    + newStatus.name() + ".Старый статус: " + oldStatus.name());
    }

    /**
     * Проверяет наличие ID счета.
     *
     * @param transactionId идентификатор транзакции
     */
    private void checkId(Long transactionId) {
        if (transactionId == null) throw new TransactionValidationException("Id не может быть пустым");
    }

    /**
     * Проверяет сумму операции, производимую в транзакции.
     *
     * @param amount сумма операции
     */
    private void checkAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new TransactionValidationException("Недопустимая сумма операции: " + amount);
    }

    /**
     * Проверяет тип операции, производимую в транзакции.
     *
     * @param transactionType тип транзакции
     */
    private void checkType(TransactionType transactionType) {
        if (transactionType == null || !SUPPORTED_TYPES.contains(transactionType))
            throw new TransactionValidationException("Недопустимый тип транзакции: " + transactionType);
    }

    /**
     * Проверяет, является ли категория допустимой.
     *
     * @param category проверяемая категория
     */
    private void checkCategory(String category) {
        if (category == null || !VALID_TRANSACTION_CATEGORIES.contains(category))
            throw new TransactionValidationException("Недопустимая категория транзакции: " + category);
    }

    /**
     * Фильтрует набор категорий, оставляя только допустимые.
     *
     * @param categories набор категорий для проверки
     * @return новый Set содержащий только валидные категории
     */
    public Set<String> validateCategories(Set<String> categories) {
        Set<String> validCategories = new HashSet<>();

        for (String category : categories) {
            if (isValidCategory(category)) validCategories.add(category);
        }
        return validCategories;
    }

    /**
     * Проверяет, является ли категория допустимой.
     *
     * @param category проверяемая категория
     * @return true - если категория допустима, false - в противном случае
     */
    private boolean isValidCategory(String category) {
        return category != null && VALID_TRANSACTION_CATEGORIES.contains(category);
    }
}