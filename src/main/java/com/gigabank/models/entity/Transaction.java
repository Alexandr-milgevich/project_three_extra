package com.gigabank.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.gigabank.constants.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction")
public class Transaction {
    //todo СДЕЛАЙ описание класса!

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long id;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "type")
    private TransactionType type;

    @Column(name = "category")
    private String category;

    @Column(name = "created_date")
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

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    Account account;
}
