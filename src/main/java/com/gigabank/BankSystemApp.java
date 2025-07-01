package com.gigabank;

import com.gigabank.models.entity.BankAccount;
import com.gigabank.models.entity.User;
import com.gigabank.service.account.BankAccountOperationService;
import com.gigabank.service.account.BankAccountService;
import com.gigabank.service.user.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.math.BigDecimal;

@EnableAspectJAutoProxy
@SpringBootApplication(scanBasePackages = {"com.gigabank", "com.example.logging"})
@ConfigurationPropertiesScan("com.example.logging.properties")
public class BankSystemApp {

    public static void main(String[] args) {
        //SpringApplication.run(BankSystemApp.class, args);
        ConfigurableApplicationContext context = SpringApplication.run(BankSystemApp.class, args);

        // Проверка загрузки аспекта
        boolean hasBean = context.containsBean("loggingAspect");
        System.out.println("LoggingAspect bean present: " + hasBean);

        UserService userService = context.getBean(UserService.class);
        BankAccountService bankAccountService = context.getBean(BankAccountService.class);
        BankAccountOperationService bankAccountOperationService = context.getBean(BankAccountOperationService.class);

        User user1 = userService.createUserEntity("Bob", "bob@gmail.com", "+7 (900) 777-77-77");
        User user2 = userService.createUserEntity("Tom", "tom@gmail.com", "+7 (900) 666-00-00");

        BankAccount bankAccount1user1 = user1.getListBankAccounts().get(0);
        BankAccount bankAccount2user1 = bankAccountService.createAccountEntity(user1);

        BankAccount bankAccount1user2 = user2.getListBankAccounts().get(0);
        BankAccount bankAccount2user2 = bankAccountService.createAccountEntity(user2);

        bankAccountOperationService.deposit(bankAccount1user1.getId(), BigDecimal.valueOf(700));
        bankAccountOperationService.deposit(bankAccount1user2.getId(), BigDecimal.valueOf(2000));

        bankAccountOperationService.transferBetweenAccounts(bankAccount1user2.getId(),
                bankAccount2user1.getId(), BigDecimal.valueOf(800));

        context.close();
    }
}