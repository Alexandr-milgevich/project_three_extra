package com.gigabank.repository;

import com.gigabank.models.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с банковскими счетами.
 * Обеспечивает доступ к данным счетов в базе данных.
 */
@Repository
public interface AccountRepository extends JpaRepository<BankAccount, Long> {
    Optional<BankAccount> findById(Long id);

    int deleteAccountById(Long id);
}