package com.gigabank;

import com.gigabank.constants.TransactionType;
import com.gigabank.db.DBManager;
import com.gigabank.models.entity.BankAccount;
import com.gigabank.models.entity.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.math.BigDecimal;

@EnableAspectJAutoProxy
@SpringBootApplication
public class BankSystemApp {

    public static void main(String[] args) {

//        SpringApplication.run(BankSystemApp.class, args);

        ConfigurableApplicationContext context = SpringApplication.run(BankSystemApp.class, args);

        DBManager dbManager = context.getBean(DBManager.class);
        User user1 = dbManager.addUser("Bob", "first@mail.corp", "+7 (911) 400-00-00");
        User user2 = dbManager.addUser("Tom", "second@mail.corp", "+7 (900) 500-00-00");

        BankAccount bankAccount1user1 = user1.getListBankAccounts().getFirst();
        BankAccount bankAccount2user1 = dbManager.addBankAccount(user1);

        BankAccount bankAccount1user2 = user2.getListBankAccounts().getFirst();
        BankAccount bankAccount2user2 = dbManager.addBankAccount(user2);

        dbManager.updateBalance(bankAccount1user1.getId(), BigDecimal.valueOf(500));
        dbManager.addTransactions(BigDecimal.valueOf(500), TransactionType.DEPOSIT.name(),
                bankAccount1user1.getId(), user1.getId(), null);

        dbManager.updateBalance(bankAccount2user2.getId(), BigDecimal.valueOf(2000));
        dbManager.addTransactions(BigDecimal.valueOf(2000), TransactionType.DEPOSIT.name(),
                bankAccount2user2.getId(), user2.getId(), null);

        dbManager.updateBalance(bankAccount1user2.getId(), BigDecimal.valueOf(1000));
        dbManager.addTransactions(BigDecimal.valueOf(1000), TransactionType.WITHDRAWAL.name(),
                bankAccount1user2.getId(), user1.getId(), user2.getId());

        dbManager.updateBalance(bankAccount2user2.getId(), BigDecimal.valueOf(-1000));
        dbManager.addTransactions(BigDecimal.valueOf(-1000), TransactionType.WITHDRAWAL.name(),
                bankAccount2user2.getId(), user1.getId(), user2.getId());

        dbManager.deleteBankAccount(bankAccount2user1.getId());

        System.out.println(dbManager.getTransactionsByUserId(user1.getId()));

        context.close();
    }
}