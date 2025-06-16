package com.gigabank.db;

import com.gigabank.exceptions.buisnes_logic.EntityNotFoundException;
import com.gigabank.exceptions.buisnes_logic.EntityValidationException;
import com.gigabank.models.entity.BankAccount;
import com.gigabank.models.entity.Transaction;
import com.gigabank.models.entity.User;
import com.gigabank.repository.AccountRepository;
import com.gigabank.repository.TransactionRepository;
import com.gigabank.repository.UserRepository;
import com.gigabank.service.account.BankAccountService;
import com.gigabank.utility.validators.ValidateAccountService;
import com.gigabank.utility.validators.ValidateTransactionService;
import com.gigabank.utility.validators.ValidateUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DBManager {

    private final BankAccountService bankAccountService;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final ValidateUserService validateUserService;
    private final ValidateAccountService validateAccountService;
    private final ValidateTransactionService validateTransactionService;

    /**
     * Создает нового пользователя в системе.
     *
     * @param username    имя пользователя
     * @param email       эл.почта
     * @param phoneNumber номер телефона
     * @return созданный пользователь с дефолтным счетом и пустым списком счетов
     */
    @Transactional
    public User addUser(String username, String email, String phoneNumber) {
        log.info("Начало создания пользователя.");

        User user = User.builder()
                .username(username)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();
        validateUserService.validateUserBeforeSaveForCreate(user);
        userRepository.save(user);
        BankAccount bankAccount = bankAccountService.createInitialAccount(user);
        user.setListBankAccounts(new ArrayList<>(List.of(bankAccount)));
        validateUserService.validateDataBeforeSave(user);

        log.info("Пользователь с id {} был создан.", user.getId());
        return userRepository.save(user);
    }

    /**
     * Возвращает список всех пользователей.
     */
    public List<User> getUsers() {
        log.info("Попытка получения всех пользователей");

        List<User> listUsers = userRepository.findAllBy()
                .orElseThrow(() -> new EntityValidationException(User.class, "Пользователи не найдены"));

        log.info("Получены все пользователи");
        return listUsers;
    }

    /**
     * Создает новый счет для пользователя.
     *
     * @param user пользователь, для которого создается счет
     * @return созданный счет с нулевым балансом
     */
    @Transactional
    public BankAccount addBankAccount(User user) {
        log.info("Начало создания нового счета пользователя");

        BankAccount bankAccount = BankAccount.builder()
                .balance(BigDecimal.ZERO)
                .user(user)
                .build();

        validateAccountService.validateUnderSave(bankAccount);
        accountRepository.save(bankAccount);

        log.info("Создан счет пользователя. Его id: {}", bankAccount.getId());
        return bankAccount;
    }

    /**
     * Удаление пользователя по идентификатору
     *
     * @param id идентификатор пользователя
     */
    public void deleteUser(Long id) {
        log.info("Попытка удаления пользователя по id: {}", id);

        if (id != null) {
            int deletedCount = userRepository.deleteUserById(id);
            if (deletedCount == 0)
                throw new EntityNotFoundException(User.class, "User not found: ");
        }

        log.info("Удален пользователь");
    }

    /**
     * Удаление счета по идентификатору
     *
     * @param id идентификатор счета
     */
    @Transactional
    public void deleteBankAccount(Long id) {
        log.info("Попытка удаления счета по id: {}", id);

        if (id != null) {
            int deletedCount = accountRepository.deleteAccountById(id);
            if (deletedCount == 0)
                throw new EntityNotFoundException(BankAccount.class, "Счет не найден: ");
        }

        log.info("Удален счет");
    }

    /**
     * Изменяет баланс счета по переданным параметрам
     *
     * @param id     идентификатор счета
     * @param amount сумма изменения баланса
     */
    @Transactional
    public void updateBalance(Long id, BigDecimal amount) {
        log.info("Попытка обновления баланса");

        if (id == null || amount == null)
            throw new EntityValidationException(User.class, "Недопустимая операция");

        BankAccount account = bankAccountService.getAccountEntityById(id);

        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            BigDecimal amountToWithdraw = amount.abs();
            if (account.getBalance().compareTo(amountToWithdraw) < 0)
                throw new EntityValidationException(BankAccount.class, "Недостаточно средств на счете для списания");
            account.setBalance(account.getBalance().subtract(amountToWithdraw));
        } else {
            account.setBalance(account.getBalance().add(amount));
        }

        validateAccountService.validateUnderSave(account);
        accountRepository.save(account);

        log.info("Баланс обновлен");
    }

    /**
     * Создает новую транзакцию на основе переданных данных.
     *
     * @param amount    - сумма изменения баланса
     * @param type      - тип транзакции
     * @param accountId - идентификатор счета
     * @param targetUserId - идентификатор пользователя кому произведена транзакция
     * @return созданная транзакция
     */
    @Transactional
    public Transaction addTransactions(BigDecimal amount, String type,
                                       Long accountId, Long targetUserId, Long sourceUserId) {
        log.info("Попытка создания транзакции");

        Transaction transaction = Transaction.builder()
                .amount(amount)
                .type(type)
                .createdDate(LocalDateTime.now())
                .bankAccount(bankAccountService.getAccountEntityById(accountId))
                .sourceUserId(sourceUserId)
                .targetUserId(targetUserId)
                .build();

        validateTransactionService.validateUnderSaveInDBManager(transaction);
        transactionRepository.save(transaction);

        log.info("Создана транзакция");
        return transaction;
    }

    /**
     * Получение списка всех транзакций.
     */
    public List<Transaction> getTransactions() {
        log.info("Попытка получения всех транзакций");

        List<Transaction> listTransactions = transactionRepository.findAllBy()
                .orElseThrow(() -> new EntityValidationException(Transaction.class, "Счета не найдены"));

        log.info("Получены все транзакции");
        return listTransactions;
    }

    /**
     * Получает транзакцию по идентификатору.
     *
     * @param id идентификатор транзакции
     * @return DTO транзакции
     */
    public Transaction getTransactionById(long id) {
        log.info("Попытка получить транзакцию по ID: {}", id);

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Transaction.class, id));

        log.info("Получена транзакцию с ID: {}", id);
        return transaction;
    }

    // Дополнительные методы, перенеся функции фильтрации и поиска в sql запросы.

    /**
     * Получает транзакции по идентификатору пользователя.
     *
     * @param userId идентификатор пользователя
     * @return список транзакций
     */
    public List<Transaction> getTransactionsByUserId(Long userId) {
        log.info("Попытка получить список транзакций по ID пользователя: {}", userId);

        List<Transaction> transactionList = transactionRepository.findTransactionsByUserId(userId);

        log.info("Получен список транзакций");
        return transactionList;
    }


    public List<Transaction> getTransactionsByAmount(BigDecimal amount) {
        return transactionRepository.findByAmount(amount);
    }

    public List<Transaction> getTransactionsByType(String type) {
        return transactionRepository.findByType(type);
    }

    public List<Transaction> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByDateRange(start, end);
    }
}