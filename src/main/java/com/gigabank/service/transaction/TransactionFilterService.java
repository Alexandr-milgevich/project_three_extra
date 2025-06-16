package com.gigabank.service.transaction;

import com.gigabank.models.dto.response.TransactionResponseDto;
import com.gigabank.models.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Сервис отвечает за управление платежами и переводами
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionFilterService {
    /**
     * Фильтрует транзакции пользователя с использованием Predicate.
     *
     * @param userResponseDto пользователь, чьи транзакции нужно отфильтровать
     * @param predicate       условие фильтрации транзакций
     * @return список транзакций, удовлетворяющих условию.
     * Возвращает пустой список, если user == null
     */
    public List<TransactionResponseDto> filterTransactions(UserResponseDto userResponseDto, Predicate<TransactionResponseDto> predicate) {
        if (userResponseDto == null) {
            return Collections.emptyList();
        }

        return userResponseDto.getListAccountDto().stream()
                .flatMap(bankAccount -> bankAccount.getListTransactionResponseDto().stream())
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Преобразует транзакции пользователя с использованием Function.
     *
     * @param userResponseDto пользователь, чьи транзакции нужно преобразовать
     * @param function        функция преобразования Transaction -> String
     * @return список строковых представлений транзакций.
     * Возвращает пустой список, если user == null
     */
    public List<String> transformTransactions(UserResponseDto userResponseDto, Function<TransactionResponseDto, String> function) {
        if (userResponseDto == null) {
            return Collections.emptyList();
        }
        return userResponseDto.getListAccountDto().stream()
                .flatMap(bankAccount -> bankAccount.getListTransactionResponseDto().stream())
                .map(function)
                .collect(Collectors.toList());

    }

    /**
     * Обрабатывает транзакции пользователя с использованием Consumer.
     *
     * @param userResponseDto пользователь, чьи транзакции нужно обработать
     * @param consumer        функция обработки транзакций
     */
    public void processTransactions(UserResponseDto userResponseDto, Consumer<TransactionResponseDto> consumer) {
        if (userResponseDto == null) {
            return;
        }

        userResponseDto.getListAccountDto().stream()
                .flatMap(bankAccount -> bankAccount.getListTransactionResponseDto().stream())
                .forEach(consumer);
    }

    /**
     * Создаёт список транзакций с использованием Supplier.
     *
     * @param supplier поставщик списка транзакций
     * @return созданный список транзакций
     */
    public List<TransactionResponseDto> createTransactionList(Supplier<List<TransactionResponseDto>> supplier) {
        return supplier.get();
    }
}