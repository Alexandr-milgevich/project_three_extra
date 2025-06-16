package com.gigabank.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gigabank.constants.TransactionCategories;
import com.gigabank.constants.TransactionType;
import com.gigabank.constants.status.TransactionStatus;
import com.gigabank.utility.converters.TransactionStatusConverter;
import jakarta.persistence.*;
import lombok.*;
import lombok.Builder.Default;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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

    @Default
    @Column(name = "transaction_uuid")
    private String transactionUuid = UUID.randomUUID().toString();

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    // Необязательные поля — зависят от источника оплаты
    @Column(name = "bank_name")
    private String bankName;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "merchant_name")
    private String merchantName;

    @Column(name = "digital_wallet_id")
    private String digitalWalletId;

    @Column(name = "merchant_category_code")
    private String merchantCategoryCode;

    @Default
    @Column(name = "status", nullable = false)
    @Convert(converter = TransactionStatusConverter.class)
    private TransactionStatus status = TransactionStatus.ACTIVE;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private BankAccount bankAccount;
}