package com.gigabank.utility.validators;

import com.gigabank.constants.status.AccountStatus;
import com.gigabank.exceptions.User.UserNotFoundException;
import com.gigabank.exceptions.account.AccountValidationException;
import com.gigabank.models.entity.BankAccount;
import com.gigabank.models.entity.User;
import com.gigabank.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

import static com.gigabank.utility.Utility.isFilled;

/**
 * Сервис для валидации данных банковского счета.
 * Выполняет проверки корректности данных перед операциями со счетами.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ValidateAccountService {
    private final UserService userService;

    /**
     * Выполняет полную валидацию счета перед сохранением.
     *
     * @param bankAccount проверяемый счет (не null)
     */
    public void validateUnderSave(BankAccount bankAccount) {
        log.info("Начало валидации счета перед сохранением.");

        checkAccount(bankAccount);
        checkId(bankAccount.getId());
        checkBalance(bankAccount.getBalance());
        checkUser(bankAccount.getUser());

        log.info("Завершение валидации счета перед сохранением.");
    }

    /**
     * Проверяет счет и сумму для совершения финансовой операции.
     *
     * @param accountId ID счета (не null)
     * @param amount    сумма операции (должна быть > 0)
     */
    public void validateUnderOperation(Long accountId, BigDecimal amount) {
        log.info("Начало валидации данных перед операцией со счетом.");

        checkAmount(amount);
        checkId(accountId);

        log.info("ValidateAccountService.validateUnderOperation: Завершение валидации данных перед операцией со счетом.");
    }

    /**
     * Проверяет два статуса счета: старый и изменяемый.
     *
     * @param newStatus статус для изменения
     * @param oldStatus первоначальный статус
     */
    public void checkAccountStatus(AccountStatus newStatus, AccountStatus oldStatus) {
        if (AccountStatus.isValid(newStatus.name()) || oldStatus == newStatus)
            throw new AccountValidationException("Недопустимый статус: " + newStatus.name());
    }

    /**
     * Проверяет счет на пустое значение.
     */
    private void checkAccount(BankAccount bankAccount) {
        if (Objects.isNull(bankAccount))
            throw new AccountValidationException("Счета не существует.");
    }

    /**
     * Проверяет наличие и валидность баланса счета.
     */
    private void checkBalance(BigDecimal balance) {
        if (Objects.isNull(balance) || balance.signum() < 0)
            throw new AccountValidationException("Недопустимое состояние баланса");
    }

    /**
     * Проверяет сумму операции, производимую со счетом.
     */
    private void checkAmount(BigDecimal amount) {
        if (Objects.isNull(amount) || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new AccountValidationException("Недопустимая сумма операции");
    }

    /**
     * Проверяет наличие ID счета.
     */
    private void checkId(Long id) {
        if (!isFilled(id)) throw new AccountValidationException("Id не может быть пустым.");
    }

    /**
     * Проверяет привязку счета к существующему пользователю.
     */
    private void checkUser(User user) {
        if (Objects.isNull(user))
            throw new AccountValidationException("Счет не может быть не привязанным к пользователю");
        if (userService.getUserById(user.getId()) == null)
            throw new UserNotFoundException("Пользователя не существует");
    }
}