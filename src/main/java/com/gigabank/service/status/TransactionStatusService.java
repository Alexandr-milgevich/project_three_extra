package com.gigabank.service.status;

import com.gigabank.constants.status.BankAccountStatus;
import com.gigabank.constants.status.TransactionStatus;
import com.gigabank.models.entity.Transaction;
import com.gigabank.service.transaction.TransactionService;
import com.gigabank.utility.validators.ValidateTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Сервис по изменению статуса транзакции
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionStatusService implements StatusChanger<Transaction, TransactionStatus>,
        StatusUpdater<List<Transaction>, BankAccountStatus> {

    private final TransactionService transactionService;
    private final ValidateTransactionService validateTransactionService;

    /**
     * Изменяет статус счета по идентификатору.
     *
     * @param id        идентификатор счета
     * @param newStatus новый статус счета
     */
    @Override
    @Transactional
    public void changeStatus(Long id, TransactionStatus newStatus) {
        log.info("Попытка изменения статуса транзакции на {} с ID: {}", newStatus, id);

        Transaction transaction = transactionService.getTransactionById(id);
        TransactionStatus oldStatus = transaction.getStatus();
        validateTransactionService.checkTransactionStatus(newStatus, oldStatus);
        transaction.setStatus(newStatus);
        transactionService.save(transaction);

        log.info("Статус транзакции изменен c {} на {} с ID: {}", oldStatus, newStatus, id);
    }

    /**
     * Изменяет статус счета при изменении состояния счета.
     *
     * @param transactionList   список транзакций счетов
     * @param bankAccountStatus новый статус счета
     */
    @Override
    @Transactional
    public void updateStatus(List<Transaction> transactionList, BankAccountStatus bankAccountStatus) {
        log.info("Начало изменений статуса для списка транзакций. Статус счета: {}", bankAccountStatus);

        if (transactionList == null || transactionList.isEmpty()) {
            log.debug("Список счетов пуст, нет необходимости обновлять статусы");
            return;
        }

        TransactionStatus transactionStatus = switch (bankAccountStatus) {
            case ACTIVE -> TransactionStatus.ACTIVE;
            case BLOCKED -> TransactionStatus.BLOCKED;
            case ARCHIVED -> TransactionStatus.ARCHIVED;
        };

        transactionList.forEach(transaction -> {
            transaction.setStatus(transactionStatus);
            transactionService.save(transaction);
        });

        log.info("Изменился статус для списка транзакций. Статус счета: {}", bankAccountStatus);
    }
}