package com.gigabank.service;

import com.gigabank.constants.status.UserStatus;
import com.gigabank.exceptions.User.UserNotFoundException;
import com.gigabank.mappers.UserMapper;
import com.gigabank.models.dto.request.user.CreateUserRequestDto;
import com.gigabank.models.dto.request.user.UpdateUserRequestDto;
import com.gigabank.models.dto.response.UserResponseDto;
import com.gigabank.models.entity.BankAccount;
import com.gigabank.models.entity.User;
import com.gigabank.repository.UserRepository;
import com.gigabank.service.account.AccountService;
import com.gigabank.utility.validators.ValidateUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для работы с пользователями банка.
 * Обеспечивает основные CRUD-операции для сущности пользователя,
 * включая создание, чтение, обновление и удаление.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final AuditService auditService;
    private final UserRepository userRepository;
    private final AccountService accountService;
    private final ValidateUserService validator;

    /**
     * Создает нового пользователя в системе.
     *
     * @param createUserRequestDto DTO с данными для создания пользователя
     * @return DTO созданного пользователя
     */
    @Transactional
    public UserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {
        log.info("Начало создания пользователя.");

        User user = userMapper.toEntity(createUserRequestDto);
        validator.checkEmailAndPhoneUniqueness(user.getEmail(), user.getPhoneNumber());
        BankAccount bankAccount = accountService.createInitialAccount(user);
        user.setListBankAccounts(List.of(bankAccount));

        save(user);

        log.info("Пользователь с id {} был создан.", user.getId());
        return userMapper.toDto(user);
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return DTO пользователя
     */
    public UserResponseDto getUserByIdFromController(Long id) {
        log.info("Попытка получить пользователя по ID: {}", id);

        UserResponseDto dto = userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден по id: " + id));

        log.info("Получен пользователь по ID: {}", id);
        return dto;
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return DTO пользователя
     */
    public User getUserById(Long id) {
        log.info("Попытка получить пользователя с ID: {}", id);

        User user = userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден по id: " + id));

        log.info("Получен пользователь с ID: {}", id);
        return user;
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param id        идентификатор пользователя
     * @param updateDto DTO с обновленными данными
     * @return DTO обновленного пользователя
     */
    @Transactional
    public UserResponseDto updateUser(Long id, UpdateUserRequestDto updateDto) {
        log.info("Обновление пользователя с ID: {}", id);

        User user = getUserById(id);
        userMapper.updateFromDto(updateDto, user);
        save(user);

        log.info("Пользователь с ID {} успешно обновлён", id);
        return userMapper.toDto(user);
    }

    /**
     * Сохраняет пользователя в базу данных.
     *
     * @param user сущность пользователя
     */
    public void save(User user) {
        log.info("Начало сохранения пользователя.");

        validator.validateDataBeforeSave(user);
        userRepository.save(user);

        log.info("Пользователь успешно сохранён с Id: {}.", user.getId());
    }

    /**
     * Изменяет статус пользователя по идентификатору.
     *
     * @param id        идентификатор пользователя
     * @param newStatus новый статус пользователя
     */
    @Transactional
    public void changeUserStatus(Long id, UserStatus newStatus, String reason) {
        log.info("Попытка изменения статуса пользователя на {} с ID: {}", newStatus, id);

        User user = getUserById(id);
        UserStatus oldStatus = user.getStatus();
        validator.checkUserStatus(newStatus, oldStatus);
        user.setStatus(newStatus);
        save(user);

        auditService.logStatusChange(id, oldStatus, newStatus, reason);
        log.info("Статус пользователя изменен c {} на {} с ID: {}", oldStatus, newStatus, id);

        accountService.updateAccountsStatus(user.getListBankAccounts(), newStatus);
    }
}