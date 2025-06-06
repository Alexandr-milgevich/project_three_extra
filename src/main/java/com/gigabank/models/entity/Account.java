package com.gigabank.models.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long id;

    @Column(name = "balance")
    private BigDecimal balance;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    User user;

    @JsonManagedReference
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Transaction> listTransactions = new ArrayList<>();
}