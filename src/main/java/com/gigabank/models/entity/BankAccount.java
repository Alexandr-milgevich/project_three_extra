package com.gigabank.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gigabank.constants.status.AccountStatus;
import com.gigabank.utility.converters.AccountStatusConverter;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "number")
    private Long id;

    @Column(name = "balance")
    private BigDecimal balance;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "status", nullable = false)
    @Convert(converter = AccountStatusConverter.class)
    private AccountStatus status = AccountStatus.ACTIVE;

    @JsonManagedReference
    @OneToMany(mappedBy = "bank_account", cascade = CascadeType.ALL)
    List<Transaction> listTransactions = new ArrayList<>();
}