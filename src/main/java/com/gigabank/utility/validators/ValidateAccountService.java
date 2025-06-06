package com.gigabank.utility.validators;

import com.gigabank.exceptions.User.UserNotFoundException;
import com.gigabank.exceptions.account.AccountNotFoundException;
import com.gigabank.exceptions.account.AccountValidationException;
import com.gigabank.models.entity.Account;
import com.gigabank.repository.AccountRepository;
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
    private final AccountRepository accountRepository;

    /**
     * Выполняет полную валидацию счета перед сохранением.
     *
     * @param account проверяемый счет (не null)
     * @throws AccountValidationException если данные счета невалидны
     * @throws UserNotFoundException      если пользователь не существует
     */
    public void validate(Account account) {
        checkAccount(account);
        checkId(account);
        checkBalance(account);
        checkUser(account);
    }

    /**
     * Проверяет счет и сумму для совершения финансовой операции.
     *
     * @param accountId ID счета (не null)
     * @param amount    сумма операции (должна быть > 0)
     * @return валидный счет
     * @throws AccountNotFoundException   если счет не найден
     * @throws AccountValidationException если сумма невалидна
     */
    public Account validateForOperation(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Счет не найден. ID: " + accountId));

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AccountValidationException("Сумма должна быть положительной");
        }

        return account;
    }

    /**
     * Проверяет счет на пустое значение.
     */
    private void checkAccount(Account account) {
        if (Objects.isNull(account)) throw new AccountValidationException("Счета не существует.");
    }

    /**
     * Проверяет наличие и валидность баланса счета.
     */
    private void checkBalance(Account account) {
        if (Objects.isNull(account.getBalance()))
            throw new AccountValidationException("Баланс не может быть пустым.");
        if (account.getBalance().signum() < 0)
            throw new AccountValidationException("Баланс не может быть отрицательным.");
    }

    /**
     * Проверяет наличие ID счета.
     */
    private void checkId(Account account) {
        if (!isFilled(account.getId())) throw new AccountValidationException("Id не может быть пустым.");
    }

    /**
     * Проверяет привязку счета к существующему пользователю.
     */
    private void checkUser(Account account) {
        if (Objects.isNull(account.getUser()))
            throw new AccountValidationException("Счет не может быть не привязанным к пользователю");
        if (userService.getUserById(account.getUser().getId()) == null)
            throw new UserNotFoundException("Пользователя не существует");
    }
}