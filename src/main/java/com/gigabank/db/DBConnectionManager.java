package com.gigabank.db;

import com.gigabank.exceptions.db.DataBaseException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

/**
 * Singleton класс для управления подключением к базе данных.
 * Загружает параметры из файла db.properties и открывает/закрывает соединение.
 */
@Slf4j
@Component
public class DBConnectionManager {

    @Getter
    private Connection connection;  //Объект соединения с базой данных (null если соединение не установлено)
    private final Properties props; //Свойства из файла конфигурации db.properties
    private static DBConnectionManager instance;

    /**
     * Приватный конструктор, который инициализирует свойства и подключается к БД.
     * Загружает файл db.properties из classpath и драйвер базы данных.
     *
     * @throws RuntimeException если файл конфигурации не найден или драйвер не загружен
     */
    private DBConnectionManager() {
        log.info("Начало загрузки конфигурации БД.");
        props = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) throw new DataBaseException("Не найден файл db.properties в classpath");
            props.load(input);
            Class.forName(props.getProperty("db.driver"));
            connect();

            log.info("Инициализация конфигурации с БД успешно завершилась.");
        } catch (IOException | ClassNotFoundException e) {
            log.error("Ошибка при загрузке конфигурации или драйвера");
            throw new RuntimeException("Ошибка при загрузке конфигурации или драйвера", e);
        }
    }

    /**
     * Метод для получения единственного экземпляра DBConnectionManager.
     * Создаёт экземпляр при первом вызове.
     *
     * @return экземпляр DBConnectionManager
     */
    public static synchronized DBConnectionManager getInstance() {
        if (instance == null) {
            instance = new DBConnectionManager();
        }
        return instance;
    }

    /**
     * Открывает соединение с базой данных, если оно ещё не открыто.
     *
     * @throws RuntimeException при ошибках подключения
     */
    public void connect() {
        try {
            log.info("Начало соединения с БД.");

            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(
                        props.getProperty("db.url"),
                        props.getProperty("db.username"),
                        props.getProperty("db.password")
                );
                System.out.println("Соединение с базой установлено.");
            }

            log.info("Соединение с БД установлено.");
        } catch (SQLException e) {
            log.error("Ошибка при подключении к базе данных");
            throw new DataBaseException("Ошибка при подключении к базе данных: " + e.getMessage());
        }
    }

    /**
     * Закрывает соединение с базой данных, если оно открыто.
     */
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Соединение с базой закрыто.");
            } catch (SQLException e) {
                log.error("Ошибка при закрытии соединения");
                throw new DataBaseException("Ошибка при закрытии соединения: " + e.getMessage());
            }
        }
    }
}
