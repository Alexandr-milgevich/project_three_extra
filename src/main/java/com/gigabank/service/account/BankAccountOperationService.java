package com.gigabank.service.account;

import com.gigabank.constants.TransactionType;
import com.gigabank.exceptions.buisnes_logic.EntityValidationException;
import com.gigabank.models.entity.BankAccount;
import com.gigabank.models.entity.Transaction;
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
public class BankAccountOperationService {
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;
    private final ValidateAccountService validateAccountService;

    /**
     * Выполняет списание средств с указанного счета
     * Для прямых вызовов (с транзакцией)
     *
     * @param id     идентификатор счета для списания
     * @param amount сумма для списания
     */
    @Transactional
    public void withdraw(Long id, BigDecimal amount) {
        doWithdraw(id, amount);
    }

    /**
     * Выполняет списание средств с указанного счета
     * Для вызовов внутри других транзакций (без аннотации).
     *
     * @param id     идентификатор счета для списания
     * @param amount сумма для списания
     */
    public void doWithdraw(Long id, BigDecimal amount) {
        log.info("Начало списание средств со счета. id: {}", id);

        validateAccountService.validateUnderOperation(id, amount);
        BankAccount account = bankAccountService.getAccountEntityById(id);

        if (account.getBalance().compareTo(amount) < 0)
            throw new EntityValidationException(BankAccount.class, "Недостаточно средств на счете для списания");

        account.setBalance(account.getBalance().subtract(amount));
        bankAccountService.save(account);

        transactionService.createTransaction(amount, TransactionType.WITHDRAWAL.name(), account);
        log.info("Произведено списание со счета на сумму: {}. id: {}", amount, id);
    }

    /**
     * Выполняет пополнение указанного счета.
     * Для прямых вызовов (с транзакцией)
     *
     * @param id     идентификатор счета для пополнения
     * @param amount сумма для пополнения
     */
    @Transactional
    public void deposit(Long id, BigDecimal amount) {
        doDeposit(id, amount);
    }

    /**
     * Выполняет пополнение указанного счета.
     * Для вызовов внутри других транзакций (без аннотации).
     *
     * @param id     идентификатор счета для пополнения
     * @param amount сумма для пополнения
     */
    public void doDeposit(Long id, BigDecimal amount) {

        log.info("Начало пополнения средств на счета Id: {}", id);

        validateAccountService.validateUnderOperation(id, amount);
        BankAccount account = bankAccountService.getAccountEntityById(id);
        account.setBalance(account.getBalance().add(amount));
        bankAccountService.save(account);

        transactionService.createTransaction(amount, TransactionType.DEPOSIT.name(), account);

        log.info("Произведено пополнение со счета на сумму: {}. id: {}", amount, id);
    }

    /**
     * Выполняет перевод средств между счетами
     *
     * @param fromAccountId ID счета отправителя
     * @param toAccountId   ID счета получателя
     * @param amount        сумма перевода
     */
    @Transactional
    public void transferBetweenAccounts(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        log.info("Начало перевода {} со счета {} на счет {}", amount, fromAccountId, toAccountId);

        if (fromAccountId.equals(toAccountId))
            throw new EntityValidationException(Transaction.class, "Нельзя переводить на тот же счет");

        doWithdraw(fromAccountId, amount);
        doDeposit(toAccountId, amount);

        log.info("Успешный перевод {} со счета {} на счет {}", amount, fromAccountId, toAccountId);
    }
}