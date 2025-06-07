package com.gigabank.service.transaction;

import com.gigabank.constants.status.AccountStatus;
import com.gigabank.constants.status.TransactionStatus;
import com.gigabank.exceptions.User.UserNotFoundException;
import com.gigabank.exceptions.transaction.TransactionNotFoundException;
import com.gigabank.mappers.TransactionMapper;
import com.gigabank.models.dto.request.transaction.CreateTransactionRequestDto;
import com.gigabank.models.dto.response.TransactionResponseDto;
import com.gigabank.models.dto.response.UserResponseDto;
import com.gigabank.models.entity.Transaction;
import com.gigabank.repository.TransactionRepository;
import com.gigabank.utility.validators.ValidateTransactionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final ValidateTransactionService validateTransactionService;
    private final TransactionMapper transactionMapper;

    /**
     * Создает новую транзакцию на основе переданных данных.
     *
     * @param createDto DTO с данными для создания транзакции
     * @return DTO созданной транзакции
     */
    @Transactional
    public TransactionResponseDto createTransaction(CreateTransactionRequestDto createDto) {
        log.info("Попытка создания транзакции: {}", createDto);

        Transaction transaction = transactionMapper.toEntityFromCreateRequestDto(createDto);
        save(transaction);

        log.info("Создана транзакция с id: {}", transaction.getId());
        return transactionMapper.toResponseDto(transaction);
    }

    /**
     * Получает транзакцию по идентификатору.
     *
     * @param id идентификатор транзакции
     * @return DTO транзакции
     */
    public TransactionResponseDto getTransactionById(long id) {
        log.info("Попытка получить транзакцию по ID: {}", id);

        TransactionResponseDto dto = transactionRepository.findByIdAndStatus(id, TransactionStatus.ACTIVE)
                .map(transactionMapper::toResponseDto)
                .orElseThrow(() -> new TransactionNotFoundException("Транзакция не найдена по id: " + id));

        log.info("Получена транзакцию с ID: {}", id);
        return dto;
    }

    public Page<Transaction> getTransactionsByAccountId(Long accountId, Pageable pageable) {
        return transactionRepository.findByAccountId(accountId, pageable);
    }

    /**
     * Сохраняет информацию о транзакции в базу данных.
     * Перед сохранением выполняет валидацию данных транзакции.
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
     * Изменяет статус счета при изменении состояния пользователя (BLOCKED, DELETED).
     *
     * @param transactions  список транзакций счетов
     * @param accountStatus новый статус счета
     */
    @Transactional
    public void updateTransactionsStatus(List<Transaction> transactions, AccountStatus accountStatus) {
        log.info("Начало изменений статуса для списка транзакций. Статус счета: {}", accountStatus);

        TransactionStatus transactionStatus = switch (accountStatus) {
            case ACTIVE -> TransactionStatus.ACTIVE;
            case BLOCKED -> TransactionStatus.BLOCKED;
            case ARCHIVED -> TransactionStatus.ARCHIVED;
        };

        transactions.forEach(transaction -> {
            transaction.setStatus(transactionStatus);
            transactionRepository.save(transaction);
        });

        log.info("Изменился статус для списка транзакций. Статус счета: {}", accountStatus);
    }

    /**
     * Изменяет статус транзакции по идентификатору.
     *
     * @param id        идентификатор транзакции
     * @param newStatus новый статус транзакции
     */
    @Transactional
    public void changeTransactionStatus(Long id, TransactionStatus newStatus, String reason) {
        log.info("Попытка изменения статуса транзакции на {} с ID: {}", newStatus, id);

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Счет не найден по id: " + id));

        TransactionStatus oldStatus = transaction.getStatus();
        validateTransactionService.checkTransactionStatus(newStatus, oldStatus);
        transaction.setStatus(newStatus);
        save(transaction);

        log.info("Статус транзакции изменен c {} на {} с ID: {}", oldStatus, newStatus, id);
    }

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