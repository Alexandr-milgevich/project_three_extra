package com.gigabank.utility.validators;

import com.gigabank.constants.status.BankAccountStatus;
import com.gigabank.constants.status.TransactionStatus;
import com.gigabank.exceptions.buisnes_logic.EntityValidationException;
import com.gigabank.models.entity.Transaction;
import com.gigabank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

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
     * Производит валидацию данных перед сохранением транзакции в БД.
     */
    public void validateUnderSave(Transaction transaction) {
        log.info("Начало валидации транзакции перед сохранением.");

        checkTargetUserId(transaction.getTargetUserId());
        checkSourceUserId(transaction.getSourceUserId());
        checkAmount(transaction.getAmount());
        checkType(transaction.getType());

        log.info("Завершение валидации транзакции перед сохранением.");
    }

    /**
     * Проверяет два статуса счета: старый и изменяемый.
     *
     * @param newStatus статус для изменения
     * @param oldStatus первоначальный статус
     */
    public void checkTransactionStatus(TransactionStatus newStatus, TransactionStatus oldStatus) {
        if (BankAccountStatus.isValid(newStatus.name()) || oldStatus == newStatus)
            throw new EntityValidationException(Transaction.class, "Недопустимый статус: " + newStatus.name());
    }

    /**
     * Проверяет идентификатор пользователя кому проведена транзакция.
     *
     * @param targetUserId идентификатор пользователя кому проведена транзакция.
     */
    private void checkTargetUserId(Long targetUserId) {
        if (Objects.isNull(targetUserId) || !userRepository.existsById(targetUserId))
            throw new EntityValidationException(Transaction.class,
                    "Данного пользователя не существует или указан неверно. ID:  " + targetUserId);
    }

    /**
     * Проверяет идентификатор пользователя от кого была проведена транзакция.
     *
     * @param sourceUserId идентификатор пользователя от кого была совершена операция.
     */
    private void checkSourceUserId(Long sourceUserId) {
        if (!Objects.isNull(sourceUserId) && !userRepository.existsById(sourceUserId))
            throw new EntityValidationException(Transaction.class,
                    "Данного пользователя не существует. ID:  " + sourceUserId);
    }

    /**
     * Проверяет сумму операции, производимую в транзакции.
     *
     * @param amount сумма операции.
     */
    private void checkAmount(BigDecimal amount) {
        if (Objects.isNull(amount) || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new EntityValidationException(Transaction.class, "Недопустимая сумма операции: " + amount);
    }

    /**
     * Проверяет тип операции, производимую в транзакции.
     *
     * @param transactionType тип транзакции.
     */
    private void checkType(String transactionType) {
        if (Objects.isNull(transactionType) || SUPPORTED_TYPES.contains(transactionType))
            throw new EntityValidationException(Transaction.class, "Недопустимый тип транзакции: " + transactionType);
    }
}