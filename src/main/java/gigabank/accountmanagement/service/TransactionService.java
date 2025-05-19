package gigabank.accountmanagement.service;

import gigabank.accountmanagement.models.dto.TransactionDto;
import gigabank.accountmanagement.models.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Сервис отвечает за управление платежами и переводами
 */
@Service
public class TransactionService {

    /**
     * Набор допустимых категорий транзакций.
     * Неизменяемый Set, не допускает null-значений.
     * Поддерживаемые категории: "Health", "Beauty", "Education".
     */
    public static final Set<String> TRANSACTION_CATEGORIES = Set.of("Health", "Beauty", "Education");

    /**
     * Проверяет, является ли категория допустимой.
     *
     * @param category проверяемая категория
     * @return true - если категория допустима, false - в противном случае
     */
    public Boolean isValidCategory(String category) {
        return category != null && TRANSACTION_CATEGORIES.contains(category);
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

    /**
     * Фильтрует транзакции пользователя с использованием Predicate.
     *
     * @param userDto   пользователь, чьи транзакции нужно отфильтровать
     * @param predicate условие фильтрации транзакций
     * @return список транзакций, удовлетворяющих условию.
     * Возвращает пустой список, если user == null
     */
    public List<TransactionDto> filterTransactions(UserDto userDto, Predicate<TransactionDto> predicate) {
        if (userDto == null) {
            return Collections.emptyList();
        }

        return userDto.getBankAccountDtos().stream()
                .flatMap(bankAccount -> bankAccount.getTransactionDtos().stream())
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Преобразует транзакции пользователя с использованием Function.
     *
     * @param userDto  пользователь, чьи транзакции нужно преобразовать
     * @param function функция преобразования Transaction -> String
     * @return список строковых представлений транзакций.
     * Возвращает пустой список, если user == null
     */
    public List<String> transformTransactions(UserDto userDto, Function<TransactionDto, String> function) {
        if (userDto == null) {
            return Collections.emptyList();
        }
        return userDto.getBankAccountDtos().stream()
                .flatMap(bankAccount -> bankAccount.getTransactionDtos().stream())
                .map(function)
                .collect(Collectors.toList());

    }

    /**
     * Обрабатывает транзакции пользователя с использованием Consumer.
     *
     * @param userDto  пользователь, чьи транзакции нужно обработать
     * @param consumer функция обработки транзакций
     */
    public void processTransactions(UserDto userDto, Consumer<TransactionDto> consumer) {
        if (userDto == null) {
            return;
        }

        userDto.getBankAccountDtos().stream()
                .flatMap(bankAccount -> bankAccount.getTransactionDtos().stream())
                .forEach(consumer);
    }

    /**
     * Создаёт список транзакций с использованием Supplier.
     *
     * @param supplier поставщик списка транзакций
     * @return созданный список транзакций
     */
    public List<TransactionDto> createTransactionList(Supplier<List<TransactionDto>> supplier) {
        return supplier.get();
    }
}
