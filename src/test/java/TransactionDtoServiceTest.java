/*
import gigabank.accountmanagement.models.dto.BankAccountDto;
import gigabank.accountmanagement.models.dto.TransactionDto;
import gigabank.accountmanagement.constants.TransactionType;
import gigabank.accountmanagement.models.dto.UserDto;
import gigabank.accountmanagement.service.transaction.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TransactionDtoServiceTest {
    private TransactionService transactionService = new TransactionService();
    private UserDto userDto;
    private BankAccountDto bankAccountDto1;
    private BankAccountDto bankAccountDto2;

    @BeforeEach
    public void setUp() {
        transactionService = new TransactionService();
        userDto = new UserDto();
        bankAccountDto1 = new BankAccountDto();
        bankAccountDto2 = new BankAccountDto();

        bankAccountDto1.getTransactionDto().add(new TransactionDto("1", new BigDecimal("100.00"), TransactionType.PAYMENT, "Category1", LocalDateTime.now()));
        bankAccountDto1.getTransactionDto().add(new TransactionDto("2", new BigDecimal("50.00"), TransactionType.PAYMENT, "Category2", LocalDateTime.now().minusDays(10)));
        bankAccountDto2.getTransactionDto().add(new TransactionDto("3", new BigDecimal("200.00"), TransactionType.PAYMENT, "Category1", LocalDateTime.now().minusMonths(1)));
        bankAccountDto2.getTransactionDto().add(new TransactionDto("4", new BigDecimal("150.00"), TransactionType.PAYMENT, "Category3", LocalDateTime.now().minusDays(5)));

        userDto.getBankAccountDtos().add(bankAccountDto1);
        userDto.getBankAccountDtos().add(bankAccountDto2);
    }

    @Test
    public void testFilterTransactions() {
        Predicate<TransactionDto> isPayment = transaction -> TransactionType.PAYMENT.equals(transaction.getType());
        List<TransactionDto> result = transactionService.filterTransactions(userDto, isPayment);
        assertEquals(4, result.size());

        Predicate<TransactionDto> isLargePayment = transaction -> transaction.getAmount().compareTo(new BigDecimal("100.00")) > 0;
        result = transactionService.filterTransactions(userDto, isLargePayment);
        assertEquals(2, result.size());
    }

    @Test
    public void testFilterTransactions_nullUser() {
        Predicate<TransactionDto> isPayment = transaction -> TransactionType.PAYMENT.equals(transaction.getType());
        List<TransactionDto> result = transactionService.filterTransactions(null, isPayment);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testTransformTransactions() {
        Function<TransactionDto, String> transactionToString = transaction -> transaction.getId() + ": " + transaction.getAmount();
        List<String> result = transactionService.transformTransactions(userDto, transactionToString);
        assertEquals(4, result.size());
        assertTrue(result.contains("1: 100.00"));
        assertTrue(result.contains("2: 50.00"));
        assertTrue(result.contains("3: 200.00"));
        assertTrue(result.contains("4: 150.00"));
    }

    @Test
    public void testTransformTransactions_nullUser() {
        Function<TransactionDto, String> transactionToString = transaction -> transaction.getId() + ": " + transaction.getAmount();
        List<String> result = transactionService.transformTransactions(null, transactionToString);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testProcessTransactions() {
        List<String> processedIds = new ArrayList<>();
        Consumer<TransactionDto> collectTransactionIds = transaction -> processedIds.add(transaction.getId());
        transactionService.processTransactions(userDto, collectTransactionIds);
        assertEquals(4, processedIds.size());
        assertTrue(processedIds.contains("1"));
        assertTrue(processedIds.contains("2"));
        assertTrue(processedIds.contains("3"));
        assertTrue(processedIds.contains("4"));
    }

    @Test
    public void testProcessTransactions_nullUser() {
        List<String> processedIds = new ArrayList<>();
        Consumer<TransactionDto> collectTransactionIds = transaction -> processedIds.add(transaction.getId());
        transactionService.processTransactions(null, collectTransactionIds);
        assertTrue(processedIds.isEmpty());
    }

    @Test
    public void testCreateTransactionList() {
        Supplier<List<TransactionDto>> transactionSupplier = () -> Arrays.asList(
                new TransactionDto("5", new BigDecimal("300.00"), TransactionType.PAYMENT, "Category1", LocalDateTime.now()),
                new TransactionDto("6", new BigDecimal("400.00"), TransactionType.PAYMENT, "Category2", LocalDateTime.now())
        );
        List<TransactionDto> result = transactionService.createTransactionList(transactionSupplier);
        assertEquals(2, result.size());
        assertEquals("5", result.get(0).getId());
        assertEquals("6", result.get(1).getId());
    }
}
*/
