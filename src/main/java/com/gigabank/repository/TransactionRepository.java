package com.gigabank.repository;

import com.gigabank.models.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    //todo СДЕЛАЙ описание класса!

}