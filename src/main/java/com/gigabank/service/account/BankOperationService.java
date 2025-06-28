package com.gigabank.service.account;

import com.gigabank.constants.TransactionType;
import com.gigabank.exceptions.buisnes_logic.EntityValidationException;
import com.gigabank.models.entity.BankAccount;
import com.gigabank.models.entity.Transaction;
import com.gigabank.service.transaction.TransactionService;
import com.gigabank.utility.validators.ValidateBankAccountService;
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
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;
    private final ValidateBankAccountService validateBankAccountService;

    /**
     * Выполняет списание средств с указанного счета
     * Для прямых вызовов (с транзакцией)
     *
     * @param id     - идентификатор счета для списания
     * @param amount - сумма для списания
     */
    @Transactional
    public void withdraw(Long id, BigDecimal amount, Long sourceUserId, Long targetUserId) {
        doWithdraw(id, amount, sourceUserId, targetUserId);
    }

    /**
     * Выполняет списание средств с указанного счета
     * Для вызовов внутри других транзакций (без аннотации).
     *
     * @param bankAccountId - идентификатор счета для списания
     * @param amount        - сумма для списания
     * @param sourceUserId  - идентификатор пользователя от кого происходит транзакция
     * @param targetUserId  - идентификатор пользователя кому произведена транзакция
     */
    private void doWithdraw(Long bankAccountId, BigDecimal amount, Long sourceUserId, Long targetUserId) {
        log.info("Попытка списания средств со счета ({}) на сумму: {}.", bankAccountId, amount);

        validateBankAccountService.validateUnderOperation(bankAccountId, amount);
        BankAccount bankAccount = bankAccountService.getAccountByIdAndStatusActive(bankAccountId);

        if (bankAccount.getBalance().compareTo(amount) < 0)
            throw new EntityValidationException(BankAccount.class, "Недостаточно средств на счете для списания");

        bankAccount.setBalance(bankAccount.getBalance().subtract(amount));
        bankAccountService.save(bankAccount);
        transactionService.createTransaction(amount, TransactionType.WITHDRAWAL.name(),
                bankAccount, sourceUserId, targetUserId);

        log.info("Произведено списание со счета ({}) на сумму: {}.", bankAccountId, amount);
    }

    /**
     * Выполняет пополнение указанного счета.
     * Для прямых вызовов (с транзакцией)
     *
     * @param bankAccountId - идентификатор счета для пополнения
     * @param amount        - сумма для пополнения
     * @param sourceUserId  - идентификатор пользователя от кого происходит транзакция
     * @param targetUserId  - идентификатор пользователя кому произведена транзакция
     */
    @Transactional
    public void deposit(Long bankAccountId, BigDecimal amount, Long sourceUserId, Long targetUserId) {
        doDeposit(bankAccountId, amount, sourceUserId, targetUserId);
    }

    /**
     * Выполняет пополнение указанного счета.
     * Для вызовов внутри других транзакций (без аннотации).
     *
     * @param bankAccountId - идентификатор счета для пополнения
     * @param amount        - сумма для пополнения
     * @param sourceUserId  - идентификатор пользователя от кого происходит транзакция
     * @param targetUserId  - идентификатор пользователя кому произведена транзакция
     */
    private void doDeposit(Long bankAccountId, BigDecimal amount, Long sourceUserId, Long targetUserId) {
        log.info("Попытка пополнения средств по счету ({}) на сумму: {}", bankAccountId, amount);

        validateBankAccountService.validateUnderOperation(bankAccountId, amount);
        BankAccount account = bankAccountService.getAccountByIdAndStatusActive(bankAccountId);
        account.setBalance(account.getBalance().add(amount));
        bankAccountService.save(account);

        transactionService.createTransaction(amount, TransactionType.DEPOSIT.name(), account,
                sourceUserId, targetUserId);

        log.info("Произведено пополнение средств по счету ({}) на сумму: {}.", bankAccountId, amount);
    }

    /**
     * Выполняет перевод средств между счетами
     *
     * @param sourceUserId    - идентификатор пользователя от кого происходит транзакция
     * @param targetUserId    - идентификатор пользователя кому произведена транзакция
     * @param sourceAccountId - идентификатор счета отправителя
     * @param targetAccountId - идентификатор счета получателя
     * @param amount          - сумма перевода
     */
    @Transactional
    public void transferBetweenAccounts(Long sourceUserId, Long targetUserId,
                                        Long sourceAccountId, Long targetAccountId, BigDecimal amount) {
        log.info("Попытка перевода средств на сумму: {} со счета {} на счет {}", amount, sourceAccountId, targetAccountId);

        if (sourceAccountId.equals(targetAccountId))
            throw new EntityValidationException(Transaction.class, "Нельзя произвести операцию на тот же счет");

        doWithdraw(sourceAccountId, amount, sourceUserId, targetUserId);
        doDeposit(targetAccountId, amount, sourceUserId, targetUserId);

        log.info("Успешный перевод на сумму: {} со счета: {} на счет: {}", amount, sourceAccountId, targetAccountId);
    }
}