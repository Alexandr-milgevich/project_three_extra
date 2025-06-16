package com.gigabank.homework.weekend4.second;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Test {

    // Метод, который будет вызываться сразу после создания бина
    @PostConstruct
    public void init() {
        log.info("2. Бин класса Test был создан и инициализирован.");
    }

    // Метод, который будет вызываться перед уничтожением бина
    @PreDestroy
    public void destroy() {
        log.info("4. Бин класса Test будет уничтожен.");
    }

    public Test() {
        log.info("1. Бин класса Test вызвал конструктор.");
    }

    public void print() {
        System.out.println("3. Вызов метода класса Test");
    }
}
