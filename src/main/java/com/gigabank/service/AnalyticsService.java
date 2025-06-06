package com.gigabank.service;

import com.gigabank.annotation.LogExecutionTime;
import com.gigabank.constants.TransactionType;
import com.gigabank.models.dto.response.TransactionResponseDto;
import com.gigabank.models.dto.request.account.AccountRequestDto;
import com.gigabank.models.dto.response.UserResponseDto;
import com.gigabank.utility.validators.ValidateAnalyticsService;
import com.gigabank.utility.validators.ValidateTransactionService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис предоставляет аналитику по операциям пользователей
 */
@Service
@RequiredArgsConstructor
public class AnalyticsService {
    private final ValidateTransactionService validateTransactionService;
    private final ValidateAnalyticsService validateAnalyticsService;

    /**
     * Вывод суммы потраченных средств на категорию за последний месяц
     *
     * @param accountDto - счет
     * @param category   - категория
     */
    @LogExecutionTime
    public BigDecimal getMonthlySpendingByCategory(AccountRequestDto accountDto, String category) {
        BigDecimal totalSum = BigDecimal.ZERO;

        if (accountDto == null || !validateAnalyticsService.hasValidCategory(category)) {
            return totalSum;
        }

        LocalDateTime oneMontAgo = LocalDateTime.now().minusMonths(1L);

        totalSum = accountDto.getListTransactionResponseDto().stream()
                .filter(transaction -> TransactionType.PAYMENT.equals(transaction.getType())
                        && StringUtils.equals(transaction.getCategory(), category)
                        && transaction.getCreatedDate().isAfter(oneMontAgo))
                .map(TransactionResponseDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalSum;
    }

    /**
     * Вывод суммы потраченных средств на n категорий за последний месяц
     * со всех счетов пользователя
     *
     * @param userResponseDto - пользователь
     * @param categories      - категории
     * @return мапа категория - сумма потраченных средств
     */
    @LogExecutionTime
    public Map<String, BigDecimal> getMonthlySpendingByCategories(UserResponseDto userResponseDto, Set<String> categories) {
        Map<String, BigDecimal> resultMap = new HashMap<>();
        Set<String> validCategories = validateTransactionService.validateCategories(categories);
        if (userResponseDto == null || validCategories.isEmpty()) {
            return resultMap;
        }

        LocalDateTime oneMontAgo = LocalDateTime.now().minusMonths(1L);

        resultMap = userResponseDto.getListAccountDto().stream()
                .flatMap(bankAccount -> bankAccount.getListTransactionResponseDto().stream())
                .filter(transaction -> TransactionType.PAYMENT.equals(transaction.getType())
                        && validCategories.contains(transaction.getCategory())
                        && transaction.getCreatedDate().isAfter(oneMontAgo))
                .collect(Collectors.groupingBy(TransactionResponseDto::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, TransactionResponseDto::getAmount, BigDecimal::add)));

        return resultMap;
    }

    /**
     * Вывод платежных операций по всем счетам и по всем категориям от наибольшей к наименьшей
     *
     * @param userResponseDto - пользователь
     * @return мапа категория - все операции совершенные по ней, отсортированные от наибольшей к наименьшей
     */
    public LinkedHashMap<String, List<TransactionResponseDto>> getTransactionHistorySortedByAmount(UserResponseDto userResponseDto) {
        LinkedHashMap<String, List<TransactionResponseDto>> resultMap = new LinkedHashMap<>();
        if (userResponseDto == null) {
            return resultMap;
        }

        resultMap = userResponseDto.getListAccountDto().stream()
                .flatMap(bankAccount -> bankAccount.getListTransactionResponseDto().stream())
                .filter(validateAnalyticsService::isValidPaymentTransaction)
                .sorted(Comparator.comparing(TransactionResponseDto::getAmount))
                .collect(Collectors.groupingBy(TransactionResponseDto::getCategory, LinkedHashMap::new, Collectors.toList()));

        return resultMap;
    }

    /**
     * Вывод последних N транзакций пользователя.
     *
     * @param userResponseDto - пользователь
     * @param n               - количество последних транзакций
     * @return LinkedHashMap, где ключом является идентификатор транзакции, а значением — объект Transaction
     */
    public List<TransactionResponseDto> getLastNTransactions(UserResponseDto userResponseDto, int n) {
        List<TransactionResponseDto> lastTransactionResponseDtos = new ArrayList<>();
        if (userResponseDto == null) {
            return lastTransactionResponseDtos;
        }

        lastTransactionResponseDtos = userResponseDto.getListAccountDto().stream()
                .flatMap(bankAccount -> bankAccount.getListTransactionResponseDto().stream())
                .sorted(Comparator.comparing(TransactionResponseDto::getCreatedDate).reversed())
                .limit(n)
                .collect(Collectors.toList());

        return lastTransactionResponseDtos;
    }

    /**
     * Вывод топ-N самых больших платежных транзакций пользователя.
     *
     * @param userResponseDto - пользователь
     * @param n               - количество топовых транзакций
     * @return PriorityQueue, где транзакции хранятся в порядке убывания их значения
     */
    @LogExecutionTime
    public PriorityQueue<TransactionResponseDto> getTopNLargestTransactions(UserResponseDto userResponseDto, int n) {
        PriorityQueue<TransactionResponseDto> transactionResponseDtoPriorityQueue =
                new PriorityQueue<>(Comparator.comparing(TransactionResponseDto::getAmount));

        if (userResponseDto == null) {
            return transactionResponseDtoPriorityQueue;
        }

        transactionResponseDtoPriorityQueue = userResponseDto.getListAccountDto().stream()
                .flatMap(bankAccount -> bankAccount.getListTransactionResponseDto().stream())
                .filter(validateAnalyticsService::isValidPaymentTransaction)
                .sorted(Comparator.comparing(TransactionResponseDto::getAmount).reversed())
                .limit(n)
                .collect(Collectors.toCollection
                        (() -> new PriorityQueue<>(Comparator.comparing(TransactionResponseDto::getAmount).reversed())));

        return transactionResponseDtoPriorityQueue;
    }
}