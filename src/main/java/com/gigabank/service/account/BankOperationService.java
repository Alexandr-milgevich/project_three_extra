package com.gigabank.service.account;

import com.gigabank.constants.TransactionCategories;
import com.gigabank.constants.TransactionType;
import com.gigabank.exceptions.buisnes_logic.EntityValidationException;
import com.gigabank.models.entity.BankAccount;
import com.gigabank.service.transaction.TransactionService;
import com.gigabank.utility.validators.ValidateAccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Сервис для работы с операционными действиями по банковским счетам.
 * Обеспечивает выполнение операций пополнения, списания и перевода средств.
 * Все изменения баланса выполняются в транзакционном контексте.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BankOperationService {
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final ValidateAccountService validateAccountService;

    /**
     * Выполняет списание средств с указанного счета.
     *
     * @param id     идентификатор счета для списания
     * @param amount сумма для списания
     */
    @Transactional
    public void withdraw(Long id, BigDecimal amount) {
        log.info("Начало списание средств со счета. id: {}", id);

        validateAccountService.validateUnderOperation(id, amount);
        BankAccount account = accountService.getAccountById(id);

        if (account.getBalance().compareTo(amount) < 0)
            throw new EntityValidationException(BankAccount.class, "Недостаточно средств на счете для списания");

        account.setBalance(account.getBalance().subtract(amount));
        accountService.save(account);
        transactionService.createTransaction(amount, String.valueOf(TransactionType.WITHDRAWAL),
                String.valueOf(TransactionCategories.OTHER), id);

        log.info("Произведено списание со счета на сумму: {}. UUID: {}", amount, account.getNumberAccount());
    }

    /**
     * Выполняет пополнение указанного счета.
     *
     * @param id     идентификатор счета для пополнения
     * @param amount сумма для пополнения
     */
    @Transactional
    public void deposit(Long id, BigDecimal amount) {
        log.info("Начало пополнения средств на счета Id: {}", id);

        validateAccountService.validateUnderOperation(id, amount);
        BankAccount account = accountService.getAccountById(id);
        account.setBalance(account.getBalance().add(amount));
        accountService.save(account);
        transactionService.createTransaction(amount, String.valueOf(TransactionType.DEPOSIT),
                String.valueOf(TransactionCategories.OTHER), id);

        log.info("Произведено пополнение со счета на сумму: {}. UUID: {}", amount, account.getNumberAccount());
    }
}