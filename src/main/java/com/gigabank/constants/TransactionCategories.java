package com.gigabank.constants;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Перечисление, определяющее категории платежей в системе.
 * <p>
 * Используется для классификации транзакций по различным категориям.
 * Поддерживаемые категории:
 * <ul>
 *   <li>{@code HEALTH} - платежи, связанные с медицинскими услугами или здоровьем</li>
 *   <li>{@code BEAUTY} - платежи, связанные с услугами красоты и ухода</li>
 *   <li>{@code EDUCATION} - платежи, связанные с образовательными услугами</li>
 *   <li>{@code OTHER} - другие категории платежей, не попадающие в вышеупомянутые</li>
 * </ul>
 * </p>
 */
@Getter
public enum TransactionCategories {
    HEALTH, BEAUTY, EDUCATION, OTHER;

    public static final Set<String> VALID_TRANSACTION_CATEGORIES =
            Arrays.stream(TransactionCategories.values())
                    .map(Enum::name)
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());
}