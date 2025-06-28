package com.gigabank.service.status;

import com.gigabank.constants.status.BankAccountStatus;
import com.gigabank.constants.status.UserStatus;
import com.gigabank.models.entity.BankAccount;
import com.gigabank.repository.BankAccountRepository;
import com.gigabank.service.account.BankAccountService;
import com.gigabank.utility.validators.ValidateBankAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис по изменению статуса банковского счета
 * и каскадного обновления его зависимости (транзакции)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccountStatusService implements StatusChanger<BankAccount, BankAccountStatus>,
        StatusUpdater<List<BankAccount>, UserStatus> {

    private final BankAccountService bankAccountService;
    private final TransactionStatusService transactionStatusService;
    private final ValidateBankAccountService validateBankAccountService;
    private final BankAccountRepository bankAccountRepository;

    /**
     * Изменяет статус счета по идентификатору.
     *
     * @param id        идентификатор счета
     * @param newStatus новый статус счета
     */
    @Override
    @Transactional
    public void changeStatus(Long id, BankAccountStatus newStatus) {
        log.info("Попытка изменения статуса счета на {} с ID: {}", newStatus, id);

        BankAccount bankAccount = bankAccountService.getAccountById(id);
        BankAccountStatus oldStatus = bankAccount.getStatus();
        validateBankAccountService.checkAccountStatus(newStatus, oldStatus);
        bankAccount.setStatus(newStatus);
        validateBankAccountService.validateUnderSave(bankAccount);
        transactionStatusService.updateStatus(bankAccount.getListTransactions(), newStatus);
        bankAccountRepository.save(bankAccount);

        log.info("Статус счета изменен c {} на {} с ID: {}", oldStatus, newStatus, id);
    }

    /**
     * Изменяет статус счета при изменении состояния пользователя.
     *
     * @param bankAccountList список счетов пользователя
     * @param userStatus      новый статус пользователя
     */
    @Override
    @Transactional
    public void updateStatus(List<BankAccount> bankAccountList, UserStatus userStatus) {
        log.info("Начало изменений статуса для списка счетов. Статус пользователя: {}", userStatus);

        if (bankAccountList == null || bankAccountList.isEmpty()) {
            log.debug("Список счетов пуст, нет необходимости обновлять статусы");
            return;
        }

        BankAccountStatus bankAccountStatus = switch (userStatus) {
            case ACTIVE -> BankAccountStatus.ACTIVE;
            case BLOCKED -> BankAccountStatus.BLOCKED;
            case DELETED -> BankAccountStatus.ARCHIVED;
        };

        bankAccountList.forEach(bankAccount -> {
            bankAccount.setStatus(bankAccountStatus);
            if (bankAccount.getListTransactions() != null && !bankAccount.getListTransactions().isEmpty()) {
                transactionStatusService.updateStatus(bankAccount.getListTransactions(), bankAccountStatus);
            }
            bankAccountService.save(bankAccount);
        });

        log.info("Изменился статус для списка счетов. Статус пользователя: {}", userStatus);
    }
}