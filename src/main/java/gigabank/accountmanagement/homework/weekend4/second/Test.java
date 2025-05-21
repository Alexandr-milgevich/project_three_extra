package gigabank.accountmanagement.homework.weekend4.second;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Test {
    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    // Метод, который будет вызываться сразу после создания бина
    @PostConstruct
    public void init() {
        logger.info("2. Бин класса Test был создан и инициализирован.");
    }

    // Метод, который будет вызываться перед уничтожением бина
    @PreDestroy
    public void destroy() {
        logger.info("4. Бин класса Test будет уничтожен.");
    }

    public Test(){
        logger.info("1. Бин класса Test вызвал конструктор.");
    }

    public void print(){
        System.out.println("3. Вызов метода класса Test");
    }
}
