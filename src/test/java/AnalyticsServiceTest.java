import gigabank.accountmanagement.models.dto.BankAccountDto;
import gigabank.accountmanagement.models.dto.TransactionDto;
import gigabank.accountmanagement.constants.TransactionType;
import gigabank.accountmanagement.models.dto.UserDto;
import gigabank.accountmanagement.service.AnalyticsService;
import gigabank.accountmanagement.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class AnalyticsServiceTest {
    private static final BigDecimal TEN_DOLLARS = new BigDecimal("10.00");
    private static final BigDecimal FIFTEEN_DOLLARS = new BigDecimal("15.00");
    private static final BigDecimal TWENTY_DOLLARS = new BigDecimal("20.00");

    private static final String BEAUTY_CATEGORY = "Beauty";
    private static final String FOOD_CATEGORY = "Food";
    private static final String EDUCATION_CATEGORY = "Education";

    private static final LocalDateTime TEN_DAYS_AGO = LocalDateTime.now().minusDays(10);
    private static final LocalDateTime FIVE_MONTHS_AGO = LocalDateTime.now().minusMonths(5);
    private static final LocalDateTime THREE_DAYS_AGO = LocalDateTime.now().minusDays(3);
    private static final LocalDateTime ONE_DAY_AGO = LocalDateTime.now().minusDays(1);

    private TransactionService transactionService = new TransactionService();
    private AnalyticsService analyticsService = new AnalyticsService(transactionService);
    private UserDto userDto = new UserDto();
    private BankAccountDto bankAccountDto1;
    private BankAccountDto bankAccountDto2;

    @BeforeEach
    public void setUp() {
        bankAccountDto1 = new BankAccountDto();
        bankAccountDto2 = new BankAccountDto();

        bankAccountDto1.getTransactionDtos().add(TransactionDto.hiddenBuilder().id("1").amount(TEN_DOLLARS).type(TransactionType.PAYMENT)
                .category(BEAUTY_CATEGORY).createdDate(TEN_DAYS_AGO).build());
        bankAccountDto1.getTransactionDtos().add(TransactionDto.hiddenBuilder().id("2").amount(FIFTEEN_DOLLARS).type(TransactionType.PAYMENT)
                .category(BEAUTY_CATEGORY).createdDate(FIVE_MONTHS_AGO).build());
        bankAccountDto2.getTransactionDtos().add(TransactionDto.hiddenBuilder().id("3").amount(TWENTY_DOLLARS).type(TransactionType.PAYMENT)
                .category(FOOD_CATEGORY).createdDate(THREE_DAYS_AGO).build());
        bankAccountDto2.getTransactionDtos().add(TransactionDto.hiddenBuilder().id("4").amount(TWENTY_DOLLARS).type(TransactionType.PAYMENT)
                .category(EDUCATION_CATEGORY).createdDate(ONE_DAY_AGO).build());

        userDto.getBankAccountDtos().add(bankAccountDto1);
        userDto.getBankAccountDtos().add(bankAccountDto2);
    }

    @Test
    public void get_monthly_spending_by_category() {
        BigDecimal result = analyticsService.getMonthlySpendingByCategory(bankAccountDto1, BEAUTY_CATEGORY);
        assertEquals(TEN_DOLLARS, result);
    }

    @Test
    public void get_monthly_spending_by_category_invalid_input() {
        // Счет равен null
        BigDecimal result = analyticsService.getMonthlySpendingByCategory(null, BEAUTY_CATEGORY);
        assertEquals(BigDecimal.ZERO, result);

        // Категория равна null
        result = analyticsService.getMonthlySpendingByCategory(bankAccountDto1, null);
        assertEquals(BigDecimal.ZERO, result);

        // Нет транзакций за последний месяц
        bankAccountDto1.getTransactionDtos().clear();
        bankAccountDto1.getTransactionDtos().add(TransactionDto.hiddenBuilder().id("5").amount(FIFTEEN_DOLLARS).type(TransactionType.PAYMENT)
                .category(BEAUTY_CATEGORY).createdDate(FIVE_MONTHS_AGO).build());
        result = analyticsService.getMonthlySpendingByCategory(bankAccountDto1, BEAUTY_CATEGORY);
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    public void get_transaction_history_sorted_by_amount() {
        LinkedHashMap<String, List<TransactionDto>> result = analyticsService.getTransactionHistorySortedByAmount(userDto);
        assertNotNull(result);
        assertEquals(1, result.get(FOOD_CATEGORY).size());
        assertEquals(2, result.get(BEAUTY_CATEGORY).size());
        assertEquals(1, result.get(EDUCATION_CATEGORY).size());

        assertEquals(TWENTY_DOLLARS, result.get(FOOD_CATEGORY).get(0).getAmount());
        assertEquals(TWENTY_DOLLARS, result.get(EDUCATION_CATEGORY).get(0).getAmount());
        assertEquals(TEN_DOLLARS, result.get(BEAUTY_CATEGORY).get(0).getAmount());
    }

    @Test
    public void get_transaction_history_sorted_by_amount_invalid_input() {
        // Пользователь равен null
        LinkedHashMap<String, List<TransactionDto>> result = analyticsService.getTransactionHistorySortedByAmount(null);
        assertTrue(result.isEmpty());

        // Нет транзакций типа PAYMENT
        userDto.getBankAccountDtos().clear();
        bankAccountDto1.getTransactionDtos().clear();
        bankAccountDto1.getTransactionDtos().add(TransactionDto.hiddenBuilder()
                .id("6").amount(TEN_DOLLARS).type(TransactionType.DEPOSIT)
                .category(BEAUTY_CATEGORY).createdDate(TEN_DAYS_AGO).build());
        userDto.getBankAccountDtos().add(bankAccountDto1);
        result = analyticsService.getTransactionHistorySortedByAmount(userDto);
        assertTrue(result.isEmpty());
    }

    @Test
    public void get_last_n_transactions() {
        List<TransactionDto> result = analyticsService.getLastNTransactions(userDto, 2);
        assertEquals(2, result.size());

        assertEquals("4", result.get(0).getId());
        assertEquals("3", result.get(1).getId());
    }

    @Test
    public void get_last_n_transactions_invalid_input() {
        List<TransactionDto> result = analyticsService.getLastNTransactions(null, 2);
        assertTrue(result.isEmpty());

        // Нет транзакций
        userDto.getBankAccountDtos().clear();
        result = analyticsService.getLastNTransactions(userDto, 2);
        assertTrue(result.isEmpty());
    }

    @Test
    public void get_top_n_largest_transactions() {
        PriorityQueue<TransactionDto> result = analyticsService.getTopNLargestTransactions(userDto, 2);
        assertEquals(2, result.size());

        TransactionDto first = result.poll();
        TransactionDto second = result.poll();

        assertEquals(TWENTY_DOLLARS, first.getAmount());
        assertEquals(TWENTY_DOLLARS, second.getAmount());
    }

    @Test
    public void get_top_n_largest_transactions_invalid_input() {
        // Пользователь равен null
        PriorityQueue<TransactionDto> result = analyticsService.getTopNLargestTransactions(null, 2);
        assertTrue(result.isEmpty());

        // Нет транзакций типа PAYMENT
        userDto.getBankAccountDtos().clear();
        bankAccountDto1.getTransactionDtos().clear();
        bankAccountDto1.getTransactionDtos().add(TransactionDto.hiddenBuilder().id("6").amount(TEN_DOLLARS).type(TransactionType.DEPOSIT)
                .category(BEAUTY_CATEGORY).createdDate(TEN_DAYS_AGO).build());
        userDto.getBankAccountDtos().add(bankAccountDto1);
        result = analyticsService.getTopNLargestTransactions(userDto, 2);
        assertTrue(result.isEmpty());
    }

    @Test
    public void analyze_performance() {

        List<TransactionDto> transactionDtos = userDto.getBankAccountDtos().stream()
                .flatMap(bankAccount -> bankAccount.getTransactionDtos().stream())
                .collect(Collectors.toList());

        long startTime = System.currentTimeMillis();
        transactionDtos.stream()
                .filter(transaction -> TransactionType.PAYMENT.equals(transaction.getType()))
                .filter(transaction -> transaction.getAmount().compareTo(new BigDecimal("1000")) > 0)
                .sorted(Comparator.comparing(TransactionDto::getAmount))
                .count();
        long endTime = System.currentTimeMillis();
        System.out.println("Sequential stream time: " + (endTime - startTime) + " ms");

        startTime = System.currentTimeMillis();
        transactionDtos.parallelStream()
                .filter(transaction -> TransactionType.PAYMENT.equals(transaction.getType()))
                .filter(transaction -> transaction.getAmount().compareTo(new BigDecimal("1000")) > 0)
                .sorted(Comparator.comparing(TransactionDto::getAmount))
                .count();
        endTime = System.currentTimeMillis();
        System.out.println("Parallel stream time: " + (endTime - startTime) + " ms");
    }
}
