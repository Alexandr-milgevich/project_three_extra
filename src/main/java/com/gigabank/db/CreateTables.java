package com.gigabank.db;

import com.gigabank.exceptions.db.DataBaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Класс для инициализации базы данных.
 * Создаёт необходимые таблицы, если они ещё не существуют.
 */
@Slf4j
@Component
public class CreateTables {

    /**
     * Метод создает таблицы в базе данных, если они ещё не существуют.
     */
    public static void initializeDatabase() {
        log.info("Попытка создания таблиц в базе данных");

        String createUserTable = """
                CREATE TABLE IF NOT EXISTS public."user" (
                    user_id BIGSERIAL PRIMARY KEY,
                    username VARCHAR(50) NOT NULL,
                    email VARCHAR(100) NOT NULL UNIQUE,
                    phone_number VARCHAR(20) NOT NULL UNIQUE
                );
                """;

        String createBankAccountTable = """
                CREATE TABLE IF NOT EXISTS public.bank_account (
                    number_account BIGSERIAL PRIMARY KEY,
                    balance DECIMAL(19, 2) NOT NULL DEFAULT 0,
                    user_id BIGINT NOT NULL,
                    version BIGSERIAL NOT NULL DEFAULT 0,
                    CONSTRAINT bank_account_fk_user FOREIGN KEY (user_id)
                        REFERENCES "user" (user_id) ON DELETE CASCADE
                );
                """;

        String createTransactionTable = """
                CREATE TABLE IF NOT EXISTS public.transaction (
                    transaction_id BIGSERIAL PRIMARY KEY,
                    amount DECIMAL(19, 2) NOT NULL,
                    type VARCHAR(36) NOT NULL,
                    created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                    number_account BIGINT NOT NULL,
                    source_id BIGINT DEFAULT NULL,
                    target_id BIGINT NOT NULL,
                    CONSTRAINT transaction_fk_bank_account FOREIGN KEY (number_account)
                        REFERENCES bank_account (number_account) ON DELETE CASCADE
                );
                """;

        String createRefundTable = """
                CREATE TABLE IF NOT EXISTS public.refund (
                    refund_id BIGSERIAL PRIMARY KEY,
                    amount DECIMAL(19, 2) NOT NULL DEFAULT 0,
                    description VARCHAR(200),
                    transaction_id BIGINT NOT NULL,
                    CONSTRAINT refund_fk_transaction FOREIGN KEY (transaction_id)
                        REFERENCES transaction (transaction_id) ON DELETE CASCADE
                );
                """;

        try (Connection connection = DBConnectionManager.getInstance().getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.executeUpdate(createUserTable);
            stmt.executeUpdate(createTransactionTable);
            stmt.executeUpdate(createBankAccountTable);
            stmt.executeUpdate(createRefundTable);

            log.info("Таблицы в базе данных созданы");
        } catch (SQLException e) {
            log.error("Ошибка при создании таблиц");
            throw new DataBaseException("Ошибка при создании таблиц в базе данных: " + e.getMessage());
        }
    }
}