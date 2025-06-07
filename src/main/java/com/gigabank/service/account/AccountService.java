package com.gigabank.service.account;

import com.gigabank.constants.status.AccountStatus;
import com.gigabank.constants.status.UserStatus;
import com.gigabank.exceptions.User.UserNotFoundException;
import com.gigabank.exceptions.account.AccountNotFoundException;
import com.gigabank.exceptions.account.AccountValidationException;
import com.gigabank.mappers.AccountMapper;
import com.gigabank.mappers.TransactionMapper;
import com.gigabank.models.dto.request.account.CreateAccountRequestDto;
import com.gigabank.models.dto.response.AccountResponseDto;
import com.gigabank.models.dto.response.TransactionResponseDto;
import com.gigabank.models.entity.BankAccount;
import com.gigabank.models.entity.Transaction;
import com.gigabank.models.entity.User;
import com.gigabank.repository.AccountRepository;
import com.gigabank.service.transaction.TransactionService;
import com.gigabank.utility.validators.ValidateAccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы с банковскими счетами.
 * Предоставляет методы для создания, управления и получения информации о счетах.
 * Обеспечивает выполнение операций пополнения, списания и перевода средств.
 * Все изменения баланса выполняются в транзакционном контексте.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountMapper accountMapper;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionService transactionService;
    private final ValidateAccountService validator;

    /**
     * Создает новый банковский счет на основе переданных данных.
     *
     * @param createDto DTO с данными для создания счета
     * @return DTO созданного счета
     */
    @Transactional
    public AccountResponseDto createAccount(CreateAccountRequestDto createDto) {
        log.info("Начало создания счета: {}", createDto);

        BankAccount bankAccount = accountMapper.toEntityFromCreateRequestDto(createDto);
        save(bankAccount);

        log.info("Создан счет с id: {}", bankAccount.getId());
        return accountMapper.toResponseDto(bankAccount);
    }

    /**
     * Создает начальный счет для нового пользователя.
     *
     * @param user пользователь, для которого создается счет
     * @return созданный счет с нулевым балансом
     */
    @Transactional
    public BankAccount createInitialAccount(User user) {
        log.info("Начало создания default счета пользователя:  {}", user);

        BankAccount bankAccount = BankAccount.builder()
                .balance(BigDecimal.ZERO)
                .user(user)
                .listTransactions(new ArrayList<>())
                .build();

        save(bankAccount);

        log.info("Создан первый счет пользователя. Его id: {}", bankAccount.getId());
        return bankAccount;
    }

    /**
     * Получает информацию о счете по его идентификатору.
     *
     * @param id идентификатор счета
     * @return DTO с информацией о счете
     */
    public AccountResponseDto getAccountById(Long id) {
        log.info("Начало поиска счета по id: {}", id);

        AccountResponseDto dto = accountRepository.findByIdAndStatus(id, AccountStatus.ACTIVE)
                .map(accountMapper::toResponseDto)
                .orElseThrow(() -> {
                    log.error("Счет не найден: {}", id);
                    return new AccountNotFoundException("Счет не найден. Его id: " + id);
                });

        log.info("Счет найден. id: {}", id);
        return dto;
    }

    /**
     * Сохраняет информацию о счете в базу данных.
     * Перед сохранением выполняет валидацию данных счета.
     *
     * @param bankAccount сущность счета для сохранения
     */
    public void save(BankAccount bankAccount) {
        log.debug("Начало сохранения счета: {}", bankAccount);

        validator.validateUnderSave(bankAccount);
        accountRepository.save(bankAccount);

        log.info("Счет был сохранен в БД. Id: {} ", bankAccount.getId());
    }

    /**
     * Получает список транзакций по указанному счету.
     *
     * @param id идентификатор счета
     * @return список DTO транзакций
     */
    public Page<TransactionResponseDto> getTransactionsByAccountIdAndPageable(Long id, Pageable pageable) {
        log.info("Получение транзакций по счету {} с пагинацией {}", id, pageable);

        BankAccount bankAccount = accountRepository.findByIdAndStatus(id, AccountStatus.ACTIVE)
                .orElseThrow(() -> new AccountNotFoundException("Счет не найден: " + id));

        Page<Transaction> transactionsPage = transactionService.getTransactionsByAccountId(bankAccount.getId(), pageable);

        return transactionsPage.map(transactionMapper::toResponseDto);
    }

    /**
     * Выполняет списание средств с указанного счета.
     *
     * @param id     идентификатор счета для списания
     * @param amount сумма для списания
     */
    @Transactional
    public void withdraw(Long id, BigDecimal amount) {
        log.info("Начало списание средств со счета. id: {}", id);

        validator.validateUnderOperation(id, amount);
        AccountResponseDto account = getAccountById(id);

        if (account.getBalance().compareTo(amount) < 0) {
            log.error("Недостаточно средств. Попытка списания средств со счета. Id: {}", account.getId());
            throw new AccountValidationException("Недостаточно средств на счете для списания");
        }

        account.setBalance(account.getBalance().subtract(amount));

        save(accountMapper.toEntityFromResponseDto(account));

        log.info("Произведено списание со счета на сумму: {}. Id: {}", amount, account.getId());
    }

    /**
     * Выполняет пополнение указанного счета.
     *
     * @param id     идентификатор счета для пополнения
     * @param amount сумма для пополнения
     */
    @Transactional
    public void deposit(Long id, BigDecimal amount) {
        log.info("Начало пополнения средств на счета Id: {}", id);

        validator.validateUnderOperation(id, amount);
        AccountResponseDto account = getAccountById(id);
        account.setBalance(account.getBalance().add(amount));

        save(accountMapper.toEntityFromResponseDto(account));

        log.info("Произведено пополнение со счета на сумму: {}. Id: {}", amount, account.getId());
    }

    /**
     * Изменяет статус счета при изменении состояния пользователя (BLOCKED, DELETED).
     *
     * @param bankAccounts список счетов пользователя
     * @param userStatus   новый статус пользователя
     */
    @Transactional
    public void updateAccountsStatus(List<BankAccount> bankAccounts, UserStatus userStatus) {
        log.info("Начало изменений статуса для списка счетов. Статус пользователя: {}", userStatus);

        AccountStatus accountStatus = switch (userStatus) {
            case ACTIVE -> AccountStatus.ACTIVE;
            case BLOCKED -> AccountStatus.BLOCKED;
            case DELETED -> AccountStatus.ARCHIVED;
        };

        bankAccounts.forEach(account -> {
            account.setStatus(accountStatus);
            transactionService.updateTransactionsStatus(account.getListTransactions(), accountStatus);
            accountRepository.save(account);
        });

        log.info("Изменился статус для списка счетов. Статус пользователя: {}", userStatus);
    }

    /**
     * Изменяет статус пользователя по идентификатору.
     *
     * @param id        идентификатор пользователя
     * @param newStatus новый статус пользователя
     */
    @Transactional
    public void changeAccountStatus(Long id, AccountStatus newStatus, String reason) {
        log.info("Попытка изменения статуса счета на {} с ID: {}", newStatus, id);

        BankAccount bankAccount = accountRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Счет не найден по id: " + id));

        AccountStatus oldStatus = bankAccount.getStatus();
        validator.checkAccountStatus(newStatus, oldStatus);
        bankAccount.setStatus(newStatus);
        save(bankAccount);

        log.info("Статус счета изменен c {} на {} с ID: {}", oldStatus, newStatus, id);

        transactionService.updateTransactionsStatus(bankAccount.getListTransactions(), newStatus);
    }
}