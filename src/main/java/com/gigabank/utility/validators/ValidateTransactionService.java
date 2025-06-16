package com.gigabank.utility.validators;

import com.gigabank.exceptions.buisnes_logic.EntityValidationException;
import com.gigabank.models.entity.Transaction;
import com.gigabank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.gigabank.constants.TransactionType.SUPPORTED_TYPES;

/**
 * Сервис для валидации данных транзакции перед сохранением в базу данных.
 * При обнаружении ошибок выбрасывает соответствующие исключения.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ValidateTransactionService {

    private final UserRepository userRepository;

    /**
     * Производит валидацию данных перед сохранением транзакции.
     */
    public void validateUnderSave(Transaction transaction) {
        log.info("Начало валидации транзакции перед сохранением.");

        checkTargetUserId(transaction.getTargetUserId());
        checkAmount(transaction.getAmount());
        checkType(transaction.getType());

        log.info("Завершение валидации транзакции перед сохранением.");
    }

    public void validateUnderSaveInDBManager(Transaction transaction) {
        log.info("Начало валидации транзакции перед сохранением из DBManager.");

        checkTargetUserId(transaction.getTargetUserId());
        checkType(transaction.getType());

        log.info("Завершение валидации транзакции перед сохранением из DBManager.");
    }

    /**
     * Проверяет идентификатор пользователя кому проведена транзакция.
     *
     * @param targetUserId идентификатор пользователя кому проведена транзакция.
     */
    private void checkTargetUserId(Long targetUserId) {
        if (targetUserId == null)
            throw new EntityValidationException(Transaction.class, "Id пользователя не может быть пустым");
        if (!userRepository.existsById(targetUserId))
            throw new EntityValidationException(Transaction.class, "Id пользователя не существует");
    }

    /**
     * Проверяет сумму операции, производимую в транзакции.
     *
     * @param amount сумма операции
     */
    private void checkAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new EntityValidationException(Transaction.class, "Недопустимая сумма операции: " + amount);
    }

    /**
     * Проверяет тип операции, производимую в транзакции.
     *
     * @param transactionType тип транзакции
     */
    private void checkType(String transactionType) {
        if (transactionType == null || !SUPPORTED_TYPES.contains(transactionType))
            throw new EntityValidationException(Transaction.class, "Недопустимый тип транзакции: " + transactionType);
    }
}