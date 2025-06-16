package com.gigabank.constants;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Перечисление типов финансовых транзакций.
 * <p>
 * Определяет возможные виды операций, которые могут выполняться в банковской системе.
 * Каждый тип транзакции имеет определенное назначение и особенности обработки.
 * </p>
 *
 * <p>Доступные типы транзакций:
 * <b>DEPOSIT</b> - операция пополнения счета
 * <b>PAYMENT</b> - операция платежа или перевода средств
 * </p>
 */
@Getter
public enum TransactionType {
    DEPOSIT, PAYMENT, WITHDRAWAL, TRANSFER;

    public static final Set<String> SUPPORTED_TYPES =
            Arrays.stream(TransactionType.values())
                    .map(Enum::name)
                    .map(String::toUpperCase)
                    .collect(Collectors.toSet());
}