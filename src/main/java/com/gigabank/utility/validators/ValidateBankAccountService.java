package com.gigabank.utility.validators;

import com.gigabank.constants.status.BankAccountStatus;
import com.gigabank.exceptions.buisnes_logic.EntityNotFoundException;
import com.gigabank.exceptions.buisnes_logic.EntityValidationException;
import com.gigabank.models.entity.BankAccount;
import com.gigabank.models.entity.Transaction;
import com.gigabank.models.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

import static com.gigabank.constants.CurrencyType.SUPPORTED_CURRENCY_TYPES;
import static com.gigabank.utility.Utility.isFilled;

/**
 * Сервис для валидации данных банковского счета.
 * Выполняет проверки корректности данных перед операциями со счетами.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ValidateBankAccountService {

    /**
     * Выполняет полную валидацию счета перед сохранением.
     *
     * @param bankAccount проверяемый счет (не null)
     */
    public void validateUnderSave(BankAccount bankAccount) {
        log.info("Начало валидации счета перед сохранением.");

        checkAccount(bankAccount);
        checkBalance(bankAccount.getBalance());
        checkUser(bankAccount.getUser());
        checkCurrency(bankAccount.getCurrency());

        log.info("Завершение валидации счета перед сохранением.");
    }

    /**
     * Проверяет счет и сумму для совершения финансовой операции.
     *
     * @param id     идентификатор счета (не null)
     * @param amount сумма операции (должна быть > 0)
     */
    public void validateUnderOperation(Long id, BigDecimal amount) {
        log.info("Начало валидации данных перед операцией со счетом.");

        checkAmount(amount);
        checkId(id);

        log.info("Завершение валидации данных перед операцией со счетом.");
    }

    /**
     * Проверяет два статуса счета: старый и изменяемый.
     *
     * @param newStatus статус для изменения
     * @param oldStatus первоначальный статус
     */
    public void checkAccountStatus(BankAccountStatus newStatus, BankAccountStatus oldStatus) {
        if (BankAccountStatus.isValid(newStatus.name()) || oldStatus == newStatus)
            throw new EntityValidationException(BankAccount.class, "Недопустимый статус: " + newStatus.name());
    }

    /**
     * Проверяет счет на пустое значение.
     */
    private void checkAccount(BankAccount bankAccount) {
        if (Objects.isNull(bankAccount))
            throw new EntityValidationException(BankAccount.class, "Счет не существует.");
    }

    /**
     * Проверяет наличие и валидность баланса счета.
     */
    private void checkBalance(BigDecimal balance) {
        if (Objects.isNull(balance) || balance.signum() < 0)
            throw new EntityValidationException(BankAccount.class, "Недопустимое состояние баланса");
    }

    /**
     * Проверяет сумму операции, производимую со счетом.
     */
    private void checkAmount(BigDecimal amount) {
        if (Objects.isNull(amount) || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new EntityValidationException(BankAccount.class, "Недопустимая сумма операции");
    }

    /**
     * Проверяет наличие ID счета.
     */
    private void checkId(Long id) {
        if (!isFilled(id))
            throw new EntityValidationException(BankAccount.class, "Id не может быть пустым.");
    }

    /**
     * Проверяет привязку счета к существующему пользователю.
     */
    private void checkUser(User user) {
        if (Objects.isNull(user))
            throw new EntityNotFoundException(BankAccount.class, "Некорректные данные пользователя");
    }

    /**
     * Проверяет тип валюты
     *
     * @param currency тип валюты.
     */
    private void checkCurrency(String currency) {
        if (Objects.isNull(currency) || SUPPORTED_CURRENCY_TYPES.contains(currency))
            throw new EntityValidationException(Transaction.class, "Недопустимый тип валюты: " + currency);
    }
}