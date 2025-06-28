package com.gigabank;

import com.gigabank.models.entity.BankAccount;
import com.gigabank.models.entity.User;
import com.gigabank.service.UserService;
import com.gigabank.service.account.BankAccountService;
import com.gigabank.service.account.BankOperationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.math.BigDecimal;

@EnableAspectJAutoProxy
@SpringBootApplication
public class BankSystemApp {

    public static void main(String[] args) {
        //SpringApplication.run(BankSystemApp.class, args);
        ConfigurableApplicationContext context = SpringApplication.run(BankSystemApp.class, args);

        UserService userService = context.getBean(UserService.class);
        BankAccountService bankAccountService = context.getBean(BankAccountService.class);
        BankOperationService bankOperationService = context.getBean(BankOperationService.class);

        User user1 = userService.createUserEntity("Bob", "bob@gmail.com", "+7 (900) 777-77-77");
        User user2 = userService.createUserEntity("Tom", "tom@gmail.com", "+7 (900) 666-00-00");

        BankAccount bankAccount1user1 = user1.getListBankAccounts().getFirst();
        BankAccount bankAccount2user1 = bankAccountService.createAccountEntity(user1);

        BankAccount bankAccount1user2 = user2.getListBankAccounts().getFirst();
        BankAccount bankAccount2user2 = bankAccountService.createAccountEntity(user2);

        bankOperationService.deposit(bankAccount1user1.getId(), BigDecimal.valueOf(700), null, user1.getId());
        bankOperationService.deposit(bankAccount1user2.getId(), BigDecimal.valueOf(2000), null, user2.getId());

        bankOperationService.transferBetweenAccounts(user2.getId(), user1.getId(), bankAccount1user2.getId(),
                bankAccount2user1.getId(), BigDecimal.valueOf(800));

        context.close();
    }
}