package com.gigabank.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gigabank.constants.status.AccountStatus;
import com.gigabank.utility.converters.AccountStatusConverter;
import jakarta.persistence.*;
import lombok.*;
import lombok.Builder.Default;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Сущность банковского счета.
 * Хранит информацию о балансе, владельце и связанных транзакциях.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bank_account")
public class BankAccount {
    @Version
    private Long version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Default
    @Column(name = "number_account")
    private String numberAccount = UUID.randomUUID().toString();

    @Column(name = "balance")
    @Default
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Default
    @Column(name = "status", nullable = false)
    @Convert(converter = AccountStatusConverter.class)
    private AccountStatus status = AccountStatus.ACTIVE;

    @Default
    @JsonManagedReference
    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL)
    private List<Transaction> listTransactions = new ArrayList<>();
}