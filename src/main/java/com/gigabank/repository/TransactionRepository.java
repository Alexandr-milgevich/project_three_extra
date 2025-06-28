package com.gigabank.repository;

import com.gigabank.constants.status.TransactionStatus;
import com.gigabank.models.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы с транзакциями.
 * Обеспечивает доступ к данным транзакций в базе данных.
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Optional<Transaction> findById(Long id);

    Optional<List<Transaction>> findAllBy();

    Optional<Transaction> findByIdAndStatus(Long id, TransactionStatus status);

    Page<Transaction> findByBankAccountId(Long accountId, Pageable pageable);

    // Найти все транзакции по конкретному идентификатору пользователя
    @Query("SELECT t FROM Transaction t WHERE t.bankAccount.user.id = :userId")
    List<Transaction> findTransactionsByUserId(@Param("userId") Long userId);

    // Найти все транзакции с конкретной суммой.
    @Query("SELECT t FROM Transaction t WHERE t.amount = :amount")
    List<Transaction> findByAmount(@Param("amount") BigDecimal amount);

    // Найти все транзакции по типу (например, перевод, пополнение и т.п.).
    @Query("SELECT t FROM Transaction t WHERE t.type = :type")
    List<Transaction> findByType(@Param("type") String type);

    // Транзакции за определённый промежуток времени.
    @Query("SELECT t FROM Transaction t WHERE t.createdDate BETWEEN :startDate AND :endDate")
    List<Transaction> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);
}