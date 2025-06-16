package com.gigabank.repository;

import com.gigabank.constants.status.TransactionStatus;
import com.gigabank.models.entity.BankAccount;
import com.gigabank.models.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с транзакциями.
 * Обеспечивает доступ к данным транзакций в базе данных.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Optional<Transaction> findByIdAndStatus(Long id, TransactionStatus status);

    Page<Transaction> findByBankAccount(BankAccount bankAccount, Pageable pageable);
}