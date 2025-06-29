package com.gigabank.constants;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum CurrencyType {
    RUB, USD, EUR;

    public static final Set<String> SUPPORTED_CURRENCY_TYPES =
            Arrays.stream(CurrencyType.values())
                    .map(Enum::name)
                    .map(String::toUpperCase)
                    .collect(Collectors.toSet());
}