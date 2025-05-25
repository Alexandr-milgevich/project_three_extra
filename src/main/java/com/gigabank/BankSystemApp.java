package com.gigabank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class BankSystemApp {

    public static void main(String[] args) {
        /*
        ConfigurableApplicationContext context = SpringApplication.run(BankSystemApp.class, args);
        MyService myService = context.getBean(MyService.class);
        myService.doSomething(); //Обязательное закрытие контекста
         */
        SpringApplication.run(BankSystemApp.class, args);

    }
}