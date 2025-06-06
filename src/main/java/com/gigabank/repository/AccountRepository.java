package com.gigabank.repository;

import com.gigabank.models.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для работы с банковскими счетами.
 * Обеспечивает доступ к данным счетов в базе данных.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findById(Long id);

    int deleteById(Long id);
}