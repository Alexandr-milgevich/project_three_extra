package com.gigabank.repository;

import com.gigabank.models.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    //todo СДЕЛАЙ описание класса!

    Optional<Account> findById(Long id);

    int deleteById(Long id);
}
