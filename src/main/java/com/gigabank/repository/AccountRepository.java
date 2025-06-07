package com.gigabank.repository;

import com.gigabank.constants.status.AccountStatus;
import com.gigabank.models.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с банковскими счетами.
 * Обеспечивает доступ к данным счетов в базе данных.
 */
@Repository
public interface AccountRepository extends JpaRepository<BankAccount, String> {
    Optional<BankAccount> findById(Long id);

    Optional<BankAccount> findByIdAndStatus(Long id, AccountStatus status);
}