package com.gigabank.service.account;

import com.gigabank.constants.status.BankAccountStatus;
import com.gigabank.exceptions.buisnes_logic.EntityNotFoundException;
import com.gigabank.mappers.BankAccountMapper;
import com.gigabank.models.dto.request.account.CreateBankAccountRequestDto;
import com.gigabank.models.dto.response.BankAccountResponseDto;
import com.gigabank.models.dto.response.TransactionResponseDto;
import com.gigabank.models.entity.BankAccount;
import com.gigabank.models.entity.User;
import com.gigabank.repository.BankAccountRepository;
import com.gigabank.service.transaction.TransactionService;
import com.gigabank.utility.validators.ValidateBankAccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Сервис для работы с банковскими счетами.
 * Предоставляет методы для создания, управления и получения информации о счетах.
 * Обеспечивает выполнение операций пополнения, списания и перевода средств.
 * Все изменения баланса выполняются в транзакционном контексте.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final BankAccountMapper bankAccountMapper;
    private final TransactionService transactionService;
    private final BankAccountRepository bankAccountRepository;
    private final ValidateBankAccountService validateBankAccountService;

    /**
     * Создает новый банковский счет на основе переданных данных.
     *
     * @param createDto DTO с данными для создания счета
     * @return DTO созданного счета
     */
    @Transactional
    public BankAccountResponseDto createAccount(CreateBankAccountRequestDto createDto) {
        log.info("Начало создания счета: {}", createDto);

        BankAccount bankAccount = bankAccountMapper.toEntityFromCreateRequestDto(createDto);
        save(bankAccount);

        log.info("Создан счет с id: {}", bankAccount.getId());
        return bankAccountMapper.toResponseDto(bankAccount);
    }

    /**
     * Создает новый банковский счет на основе переданных данных.
     *
     * @param user пользователь
     * @return сущность созданного счета
     */
    @Transactional
    public BankAccount createAccountEntity(User user) {
        log.info("Попытка создания нового счета.");

        BankAccount bankAccount = BankAccount.builder()
                .user(user)
                .build();

        save(bankAccount);

        log.info("Создан счет. id: {}", bankAccount.getId());
        return bankAccount;
    }

    /**
     * Создает начальный счет для нового пользователя.
     *
     * @param user пользователь, для которого создается счет
     * @return созданный счет с нулевым балансом
     */
    @Transactional
    public BankAccount createInitialAccount(User user) {
        log.info("Попытка создания первого счета пользователя:  {}", user);

        BankAccount bankAccount = BankAccount.builder()
                .balance(BigDecimal.ZERO)
                .user(user)
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
    public BankAccountResponseDto getAccountByIdFromController(Long id) {
        log.info("Попытка поиска счета по id: {}", id);

        BankAccountResponseDto dto = bankAccountRepository.findByIdAndStatus(id, BankAccountStatus.ACTIVE)
                .map(bankAccountMapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException(BankAccount.class, id));

        log.info("Счет найден. id: {}", id);
        return dto;
    }

    /**
     * Получает информацию о счете по его идентификатору и статусу ACTIVE.
     *
     * @param id идентификатор счета
     * @return счет
     */
    public BankAccount getAccountByIdAndStatusActive(Long id) {
        log.info("Попытка поиска счета по id: {} и статуса Active", id);

        BankAccount account = bankAccountRepository.findByIdAndStatus(id, BankAccountStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException(BankAccount.class, id));

        log.info("Счет найден. Его Id: {}", id);
        return account;
    }

    /**
     * Получает информацию о счете по его идентификатору без статуса.
     *
     * @param id идентификатор счета
     * @return счет
     */
    public BankAccount getAccountById(Long id) {
        log.info("Попытка поиска счета с id: {}", id);

        BankAccount account = bankAccountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(BankAccount.class, id));

        log.info("Счет найден. Его id: {}", id);
        return account;
    }

    /**
     * Сохраняет информацию о счете в базу данных.
     * Перед сохранением выполняет валидацию данных счета.
     *
     * @param bankAccount сущность счета для сохранения
     */
    public void save(BankAccount bankAccount) {
        log.debug("Попытка сохранения счета: {}", bankAccount);

        validateBankAccountService.validateUnderSave(bankAccount);
        bankAccountRepository.save(bankAccount);

        log.info("Счет был сохранен в БД. Id: {} ", bankAccount.getId());
    }

    /**
     * Получает список транзакций по указанному счету.
     *
     * @param accountId идентификатор счета
     * @return список DTO транзакций
     */
    public Page<TransactionResponseDto> getTransactionsByAccountIdAndPageable(Long accountId, Pageable pageable) {
        log.info("Попытка получения транзакций по счету {} с пагинацией {}", accountId, pageable);

        Page<TransactionResponseDto> transactionsPage = transactionService
                .getTransactionsByAccountIdAndPageable(accountId, pageable);

        log.info("Получены транзакции с пагинацией");
        return transactionsPage;
    }
}