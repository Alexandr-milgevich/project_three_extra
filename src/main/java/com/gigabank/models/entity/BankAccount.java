package com.gigabank.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gigabank.constants.status.BankAccountStatus;
import com.gigabank.utility.converters.BankAccountStatusConverter;
import jakarta.persistence.*;
import lombok.*;
import lombok.Builder.Default;

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
    @Column(name = "number_account")
    private Long id;

    @Column(name = "balance")
    @Default
    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Default
    @Column(name = "status", nullable = false)
    @Convert(converter = BankAccountStatusConverter.class)
    private BankAccountStatus status = BankAccountStatus.ACTIVE;

    @Default
    @JsonManagedReference
    @OneToMany(mappedBy = "bankAccount", cascade = CascadeType.ALL)
    private List<Transaction> listTransactions = new ArrayList<>();
}