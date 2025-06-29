package com.gigabank.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gigabank.constants.TransactionType;
import com.gigabank.constants.status.TransactionStatus;
import com.gigabank.utility.converters.TransactionStatusConverter;
import jakarta.persistence.*;
import lombok.*;
import lombok.Builder.Default;
import org.hibernate.annotations.CreationTimestamp;

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

    @Default
    @Column(name = "type", nullable = false)
    private String type = String.valueOf(TransactionType.PAYMENT);

    @Column(name = "created_date", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdDate;

    @Default
    @Column(name = "status", nullable = false)
    @Convert(converter = TransactionStatusConverter.class)
    private TransactionStatus status = TransactionStatus.ACTIVE;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "number_account", referencedColumnName = "number_account")
    private BankAccount bankAccount;
}