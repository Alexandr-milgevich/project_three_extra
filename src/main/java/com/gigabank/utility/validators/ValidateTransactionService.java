package com.gigabank.utility.validators;

import com.gigabank.constants.TransactionCategories;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class ValidateTransactionService {

    /**
     * Проверяет, является ли категория допустимой.
     *
     * @param category проверяемая категория
     * @return true - если категория допустима, false - в противном случае
     */
    public Boolean isValidCategory(String category) {
        return category != null && Arrays.stream(TransactionCategories.values())
                .anyMatch(c -> c.name().equalsIgnoreCase(category));
    }

    /**
     * Фильтрует набор категорий, оставляя только допустимые.
     *
     * @param categories набор категорий для проверки
     * @return новый Set содержащий только валидные категории
     */
    public Set<String> validateCategories(Set<String> categories) {
        Set<String> validCategories = new HashSet<>();

        for (String category : categories) {
            if (isValidCategory(category)) validCategories.add(category);
        }
        return validCategories;
    }
}
