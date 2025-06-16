package com.gigabank.utility;

import java.util.Objects;

/**
 * Утилитарный класс с методами для проверки заполненности значений.
 */
public class Utility {
    /**
     * Проверяет, что строка не является пустой и содержит не только пробелы.
     *
     * @param value строка для проверки
     * @return {@code true}, если строка не не пустая после удаления пробелов, иначе {@code false}
     */
    public static boolean isFilled(String value) {
        return !Objects.isNull(value) && !value.isBlank();
    }

    /**
     * Проверяет, что объект типа {@link Long} не равен {@code null}.
     *
     * @param value значение типа {@link Long} для проверки
     * @return {@code true}, если значение не {@code null}, иначе {@code false}
     */
    public static boolean isFilled(Long value) {
        return value != null;
    }
}