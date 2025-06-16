package com.gigabank.service;

import com.gigabank.exceptions.buisnes_logic.EntityNotFoundException;
import com.gigabank.mappers.UserMapper;
import com.gigabank.models.dto.request.user.CreateUserRequestDto;
import com.gigabank.models.dto.request.user.UpdateUserRequestDto;
import com.gigabank.models.dto.response.UserResponseDto;
import com.gigabank.models.entity.BankAccount;
import com.gigabank.models.entity.User;
import com.gigabank.repository.UserRepository;
import com.gigabank.service.account.BankAccountService;
import com.gigabank.utility.validators.ValidateUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private final UserRepository userRepository;
    private final BankAccountService bankAccountService;
    private final ValidateUserService validateUserService;

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
        validateUserService.validateUserBeforeSaveForCreate(user);
        userRepository.save(user);
        BankAccount bankAccount = bankAccountService.createInitialAccount(user);
        user.setListBankAccounts(new ArrayList<>(List.of(bankAccount)));
        userRepository.save(user);

        log.info("Пользователь с id {} был создан.", user.getId());
        return userMapper.toResponseDto(user);
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return DTO пользователя
     */
    public UserResponseDto getUserByIdFromController(Long id) {
        log.info("Попытка получить пользователя по ID: {}", id);

        UserResponseDto dto = userRepository.findById(id)
                .map(userMapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));

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

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));

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
        return userMapper.toResponseDto(user);
    }

    /**
     * Сохраняет пользователя в базу данных.
     *
     * @param user сущность пользователя
     */
    public void save(User user) {
        log.info("Начало сохранения пользователя.");

        validateUserService.validateDataBeforeSave(user);
        userRepository.save(user);

        log.info("Пользователь успешно сохранён с Id: {}.", user.getId());
    }

    /**
     * Каскадное удаление пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     */
    @Transactional
    public void deleteUser(Long id) {
        log.info("Попытка удаления пользователя по id: {}", id);

        if (id != null) {
            int deletedCount = userRepository.deleteUserById(id);
            if (deletedCount == 0)
                throw new EntityNotFoundException(User.class, "Пользователь не найден: ");
        }

        log.info("Удален пользователь");
    }
}