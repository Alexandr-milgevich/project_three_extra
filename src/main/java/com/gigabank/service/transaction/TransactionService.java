package com.gigabank.service.transaction;

import com.gigabank.constants.status.TransactionStatus;
import com.gigabank.exceptions.buisnes_logic.EntityNotFoundException;
import com.gigabank.exceptions.buisnes_logic.EntityValidationException;
import com.gigabank.mappers.TransactionMapper;
import com.gigabank.models.dto.request.transaction.CreateTransactionRequestDto;
import com.gigabank.models.dto.response.TransactionResponseDto;
import com.gigabank.models.entity.BankAccount;
import com.gigabank.models.entity.Transaction;
import com.gigabank.repository.TransactionRepository;
import com.gigabank.utility.validators.ValidateTransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для управления транзакциями (платежами, переводами).
 * Обеспечивает создание, валидацию, поиск и обработку транзакций.
 * Все операции с балансом выполняются в транзакционном контексте.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final ValidateTransactionService validateTransactionService;

    /**
     * Создает новую транзакцию на основе DTO запроса.
     *
     * @param createDto DTO с данными для создания транзакции
     * @return DTO созданной транзакции
     */
    @Transactional
    public TransactionResponseDto createTransactionFromController(CreateTransactionRequestDto createDto) {
        log.info("Попытка создания транзакции: {}", createDto);

        Transaction transaction = transactionMapper.toEntityFromCreateRequestDto(createDto);
        save(transaction);

        log.info("Создана транзакция с id: {}", transaction.getId());
        return transactionMapper.toResponseDto(transaction);
    }

    /**
     * Создает новую транзакцию с указанными параметрами.
     *
     * @param amount      сумма операции (должна быть положительной)
     * @param type        тип транзакции (DEPOSIT, WITHDRAWAL, TRANSFER и т.д.)
     * @param bankAccount связанный банковский счет
     * @return DTO созданной транзакции
     */
    @Transactional
    public TransactionResponseDto createTransaction(BigDecimal amount, String type, BankAccount bankAccount) {
        log.info("Попытка создания транзакции.");

        Transaction transaction = Transaction.builder()
                .amount(amount)
                .type(type)
                .createdDate(LocalDateTime.now())
                .bankAccount(bankAccount)
                .build();

        save(transaction);

        log.info("Создана транзакция. Id: {}", transaction.getId());
        return transactionMapper.toResponseDto(transaction);
    }


    /**
     * Сохраняет транзакцию в базу данных после валидации.
     *
     * @param transaction сущность транзакции для сохранения
     */
    public void save(Transaction transaction) {
        log.debug("Начало сохранения транзакции: {}", transaction);

        validateTransactionService.validateUnderSave(transaction);
        transactionRepository.save(transaction);

        log.info("Транзакция была сохранена в БД. Id: {} ", transaction.getId());
    }

    /**
     * Получает активную транзакцию по ID для контроллера.
     *
     * @param id ID транзакции
     * @return DTO транзакции
     */
    public TransactionResponseDto getTransactionByIdFromController(long id) {
        log.info("Попытка получить транзакцию по ID: {}", id);

        TransactionResponseDto dto = transactionRepository.findByIdAndStatus(id, TransactionStatus.ACTIVE)
                .map(transactionMapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException(Transaction.class, id));

        log.info("Получена транзакцию с ID: {}", id);
        return dto;
    }

    /**
     * Получает активную транзакцию по ID.
     *
     * @param id ID транзакции
     * @return сущность транзакции
     */
    public Transaction getTransactionByIdAndStatusActive(long id) {
        log.info("Попытка получить транзакцию по ID: {} и статуса Active", id);

        Transaction transaction = transactionRepository.findByIdAndStatus(id, TransactionStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException(Transaction.class, id));

        log.info("Получена транзакцию. Его ID: {}", id);
        return transaction;
    }

    /**
     * Получает транзакцию по ID без проверки статуса.
     *
     * @param id ID транзакции
     * @return сущность транзакции
     */
    public Transaction getTransactionById(long id) {
        log.info("Попытка получить транзакцию c ID: {}", id);

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Transaction.class, id));

        log.info("Получена транзакцию по ID: {}", id);
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
     * Получает страницу транзакций по ID счета с пагинацией.
     *
     * @param accountId ID банковского счета
     * @param pageable  параметры пагинации
     * @return страница с DTO транзакций
     */
    public Page<TransactionResponseDto> getTransactionsByAccountIdAndPageable(Long accountId, Pageable pageable) {
        log.info("Получение транзакций по счету {} с пагинацией {}", accountId, pageable);

        Page<TransactionResponseDto> transactionsPage = transactionRepository
                .findByBankAccountId(accountId, pageable)
                .map(transactionMapper::toResponseDto);

        log.info("Получены транзакции по счету с пагинацией по ID: {}", accountId);
        return transactionsPage;
    }

    /**
     * Получает страницу транзакций по ID пользователя с пагинацией.
     *
     * @param userId   ID пользователя
     * @param pageable параметры пагинации
     * @return страница с DTO транзакций
     */
    public Page<TransactionResponseDto> getTransactionsByUserIdAndPageable(Long userId, Pageable pageable) {
        log.info("Попытка получения транзакций по идентификатору пользователя ({}) с пагинацией {}", userId, pageable);

        Page<TransactionResponseDto> transactionsPage = transactionRepository
                .findByBankAccount_User_Id(userId, pageable)
                .map(transactionMapper::toResponseDto);

        log.info("Получены транзакции по id пользователя ({}) с пагинацией.", userId);
        return transactionsPage;
    }

    /**
     * Получает список транзакций по ID пользователя.
     *
     * @param userId ID пользователя
     * @return список транзакций
     */
    public List<Transaction> getTransactionsByUserId(Long userId) {
        log.info("Попытка получить список транзакций по ID пользователя: {}", userId);

        List<Transaction> transactionList = transactionRepository.findTransactionsByUserId(userId);

        log.info("Получен список транзакций");
        return transactionList;
    }

    /**
     * Получает транзакции по сумме.
     *
     * @param amount сумма для поиска
     * @return список транзакций с указанной суммой
     */
    public List<Transaction> getTransactionsByAmount(BigDecimal amount) {
        return transactionRepository.findByAmount(amount);
    }

    /**
     * Получает транзакции по типу.
     *
     * @param type тип транзакции
     * @return список транзакций указанного типа
     */
    public List<Transaction> getTransactionsByType(String type) {
        return transactionRepository.findByType(type);
    }

    /**
     * Получает транзакции за указанный период.
     *
     * @param start начало периода
     * @param end   конец периода
     * @return список транзакций в указанном временном диапазоне
     */
    public List<Transaction> getTransactionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByDateRange(start, end);
    }
}