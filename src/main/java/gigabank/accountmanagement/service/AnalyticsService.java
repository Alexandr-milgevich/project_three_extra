package gigabank.accountmanagement.service;

import gigabank.accountmanagement.annotations.LogExecutionTime;
import gigabank.accountmanagement.constants.TransactionType;
import gigabank.accountmanagement.models.dto.BankAccountDto;
import gigabank.accountmanagement.models.dto.TransactionDto;
import gigabank.accountmanagement.models.dto.UserDto;
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
    private final TransactionService transactionService;

    /**
     * Вывод суммы потраченных средств на категорию за последний месяц
     *
     * @param bankAccountDTO - счет
     * @param category       - категория
     */
    @LogExecutionTime
    public BigDecimal getMonthlySpendingByCategory(BankAccountDto bankAccountDTO, String category) {
        BigDecimal totalSum = BigDecimal.ZERO;

        if (bankAccountDTO == null || !transactionService.isValidCategory(category)) {
            return totalSum;
        }

        LocalDateTime oneMontAgo = LocalDateTime.now().minusMonths(1L);

        totalSum = bankAccountDTO.getTransactionDtos().stream()
                .filter(transaction -> TransactionType.PAYMENT.equals(transaction.getType())
                        && StringUtils.equals(transaction.getCategory(), category)
                        && transaction.getCreatedDate().isAfter(oneMontAgo))
                .map(TransactionDto::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalSum;
    }

    /**
     * Вывод суммы потраченных средств на n категорий за последний месяц
     * со всех счетов пользователя
     *
     * @param userDto    - пользователь
     * @param categories - категории
     * @return мапа категория - сумма потраченных средств
     */
    @LogExecutionTime
    public Map<String, BigDecimal> getMonthlySpendingByCategories(UserDto userDto, Set<String> categories) {
        Map<String, BigDecimal> resultMap = new HashMap<>();
        Set<String> validCategories = transactionService.validateCategories(categories);
        if (userDto == null || validCategories.isEmpty()) {
            return resultMap;
        }

        LocalDateTime oneMontAgo = LocalDateTime.now().minusMonths(1L);

        resultMap = userDto.getBankAccountDtos().stream()
                .flatMap(bankAccount -> bankAccount.getTransactionDtos().stream())
                .filter(transaction -> TransactionType.PAYMENT.equals(transaction.getType())
                        && validCategories.contains(transaction.getCategory())
                        && transaction.getCreatedDate().isAfter(oneMontAgo))
                .collect(Collectors.groupingBy(TransactionDto::getCategory,
                        Collectors.reducing(BigDecimal.ZERO, TransactionDto::getAmount, BigDecimal::add)));

        return resultMap;
    }

    /**
     * Вывод платежных операций по всем счетам и по всем категориям от наибольшей к наименьшей
     *
     * @param userDto - пользователь
     * @return мапа категория - все операции совершенные по ней, отсортированные от наибольшей к наименьшей
     */
    public LinkedHashMap<String, List<TransactionDto>> getTransactionHistorySortedByAmount(UserDto userDto) {
        LinkedHashMap<String, List<TransactionDto>> resultMap = new LinkedHashMap<>();
        if (userDto == null) {
            return resultMap;
        }

        resultMap = userDto.getBankAccountDtos().stream()
                .flatMap(bankAccount -> bankAccount.getTransactionDtos().stream())
                .filter(transaction -> TransactionType.PAYMENT.equals(transaction.getType()))
                .sorted(Comparator.comparing(TransactionDto::getAmount))
                .collect(Collectors.groupingBy(TransactionDto::getCategory, LinkedHashMap::new, Collectors.toList()));

        return resultMap;
    }

    /**
     * Вывод последних N транзакций пользователя.
     *
     * @param userDto - пользователь
     * @param n       - количество последних транзакций
     * @return LinkedHashMap, где ключом является идентификатор транзакции, а значением — объект Transaction
     */
    public List<TransactionDto> getLastNTransactions(UserDto userDto, int n) {
        List<TransactionDto> lastTransactionDtos = new ArrayList<>();
        if (userDto == null) {
            return lastTransactionDtos;
        }

        lastTransactionDtos = userDto.getBankAccountDtos().stream()
                .flatMap(bankAccount -> bankAccount.getTransactionDtos().stream())
                .sorted(Comparator.comparing(TransactionDto::getCreatedDate).reversed())
                .limit(n)
                .collect(Collectors.toList());

        return lastTransactionDtos;
    }

    /**
     * Вывод топ-N самых больших платежных транзакций пользователя.
     *
     * @param userDto - пользователь
     * @param n       - количество топовых транзакций
     * @return PriorityQueue, где транзакции хранятся в порядке убывания их значения
     */
    @LogExecutionTime
    public PriorityQueue<TransactionDto> getTopNLargestTransactions(UserDto userDto, int n) {
        PriorityQueue<TransactionDto> transactionDtoPriorityQueue =
                new PriorityQueue<>(Comparator.comparing(TransactionDto::getAmount));

        if (userDto == null) {
            return transactionDtoPriorityQueue;
        }

        transactionDtoPriorityQueue = userDto.getBankAccountDtos().stream()
                .flatMap(bankAccount -> bankAccount.getTransactionDtos().stream())
                .filter(transaction -> TransactionType.PAYMENT.equals(transaction.getType()))
                .sorted(Comparator.comparing(TransactionDto::getAmount).reversed())
                .limit(n)
                .collect(Collectors.toCollection
                        (() -> new PriorityQueue<>(Comparator.comparing(TransactionDto::getAmount).reversed())));

        return transactionDtoPriorityQueue;
    }
}

