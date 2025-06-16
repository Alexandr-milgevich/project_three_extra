package com.gigabank.utility.validators;

import com.gigabank.constants.TransactionCategories;
import com.gigabank.constants.TransactionType;
import com.gigabank.models.dto.response.TransactionResponseDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

/**
 * Сервис для валидации данных транзакции перед сохранением в базу данных.
 * При обнаружении ошибок выбрасывает соответствующие исключения.
 */
@Service
public class ValidateAnalyticsService {
    /**
     * Проверяет валидность платежной транзакции по следующим критериям:
     * 1. Транзакция не должна быть null
     * 2. Тип транзакции должен быть PAYMENT
     * 3. Категория транзакции должна быть не-null и не пустой
     * 4. Сумма транзакции должна быть положительной
     * 5. Дата транзакции должна быть указана
     *
     * @param transaction проверяемая транзакция
     * @return true если транзакция соответствует всем критериям, false в противном случае
     */
    public boolean isValidPaymentTransaction(TransactionResponseDto transaction) {
        return isTransactionNotNull(transaction)
                && isPaymentType(transaction)
                && hasPositiveAmount(transaction)
                && hasValidDate(transaction);
    }

    /**
     * Проверяет валидность платежной транзакции по следующим критериям:
     * 1. Транзакция не должна быть null
     * 2. Тип транзакции должен быть PAYMENT
     * 3. Категория транзакции должна быть не-null и не пустой
     * 4. Сумма транзакции должна быть положительной
     * 5. Дата транзакции должна быть указана
     *
     * @param transaction проверяемая транзакция
     * @return true если транзакция соответствует всем критериям, false в противном случае
     */
    public boolean isValidDepositTransaction(TransactionResponseDto transaction) {
        return isTransactionNotNull(transaction)
                && isDepositType(transaction)
                && hasPositiveAmount(transaction)
                && hasValidDate(transaction);
    }

    /**
     * Проверяет что переданная транзакция не является null
     *
     * @param transaction проверяемая транзакция
     * @return true если транзакция не null, false в противном случае
     */
    public boolean isTransactionNotNull(TransactionResponseDto transaction) {
        return !Objects.isNull(transaction);
    }

    /**
     * Проверяет что тип транзакции соответствует PAYMENT
     *
     * @param transaction проверяемая транзакция
     * @return true если тип транзакции PAYMENT, false в противном случае
     */
    public boolean isPaymentType(TransactionResponseDto transaction) {
        return transaction.getType().equals(TransactionType.PAYMENT);
    }

    /**
     * Проверяет что тип транзакции соответствует DEPOSIT
     *
     * @param transaction проверяемая транзакция
     * @return true если тип транзакции DEPOSIT, false в противном случае
     */
    public boolean isDepositType(TransactionResponseDto transaction) {
        return transaction.getType().equals(TransactionType.DEPOSIT);
    }

    /**
     * Проверяет валидность категории транзакции.
     * Категория считается валидной если:
     * 1. Не является null
     * 2. Присутствует в списке TransactionCategories
     *
     * @param category проверяемая категория
     * @return true, если категория валидна, false в противном случае
     */
    public boolean hasValidCategory(String category) {
        return !Objects.isNull(category)
                && !category.isEmpty()
                && Arrays.stream(TransactionCategories.values())
                .anyMatch(c -> c.name().equalsIgnoreCase(category));

    }

    /**
     * Проверяет валидность суммы транзакции:
     * - сумма не должна быть null
     * - сумма должна быть положительной
     *
     * @param transaction проверяемая транзакция
     * @return true если сумма валидна, false в противном случае
     */
    public boolean hasPositiveAmount(TransactionResponseDto transaction) {
        BigDecimal amount = transaction.getAmount();
        return !Objects.isNull(amount)
                && (amount.compareTo(BigDecimal.ZERO) > 0);
    }

    /**
     * Проверяет что дата транзакции указана
     *
     * @param transaction проверяемая транзакция
     * @return true если дата транзакции не null, false в противном случае
     */
    public boolean hasValidDate(TransactionResponseDto transaction) {
        return !Objects.isNull(transaction.getCreatedDate());
    }
}