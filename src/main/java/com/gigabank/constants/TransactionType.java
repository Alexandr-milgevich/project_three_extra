package com.gigabank.constants;

import lombok.Getter;

import java.util.EnumSet;

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

    public static final EnumSet<TransactionType> SUPPORTED_TYPES =
            EnumSet.of(TransactionType.DEPOSIT, TransactionType.PAYMENT,
                    TransactionType.WITHDRAWAL, TransactionType.TRANSFER);
}