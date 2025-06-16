package com.gigabank.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Сущность Транзакции.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "source_id")
    private Long sourceUserId;

    @Column(name = "target_id", nullable = false)
    private Long targetUserId;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "number_account", referencedColumnName = "number_account")
    private BankAccount bankAccount;
}