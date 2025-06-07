package com.gigabank.constants;

import lombok.Getter;

/**
 * Перечисление, определяющее типы платежей в системе.
 * <p>
 * Используется для классификации платежных операций.
 * Поддерживаемые типы:
 * {@code CARD} - платеж с использованием банковской карты
 * {@code WALLET} - платеж через электронный кошелек
 * {@code BANK} - банковский перевод
 * </p>
 */
@Getter
public enum PaymentType {
    CARD, WALLET, BANK
}