package com.gigabank.service;

import com.gigabank.constants.status.UserStatus;
import com.gigabank.exceptions.buisnes_logic.EntityNotFoundException;
import com.gigabank.exceptions.buisnes_logic.EntityValidationException;
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

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BankAccountService bankAccountService;
    private final ValidateUserService validateUserService;

    /**
     * Создает нового пользователя в системе.
     *
     * @param createUserRequestDto DTO с данными для создания пользователя
     * @return DTO созданного пользователя
     */
    @Transactional
    public UserResponseDto createUserFromController(CreateUserRequestDto createUserRequestDto) {
        log.info("Начало создания пользователя.");

        User user = userMapper.toEntity(createUserRequestDto);
        validateUserService.validateUserBeforeSaveForCreate(user);
        userRepository.save(user);

        BankAccount bankAccount = bankAccountService.createInitialAccount(user);
        user.setListBankAccounts(new ArrayList<>(List.of(bankAccount)));
        userRepository.save(user);

        log.info("Пользователь с id {} был создан.", user.getId());
        return userMapper.toDto(user);
    }

    /**
     * Создает нового пользователя в системе.
     *
     * @param username    - имя пользователя
     * @param email       - эл.почта
     * @param phoneNumber - номер телефона
     * @return DTO созданного пользователя
     */
    @Transactional
    public User createUserEntity(String username, String email, String phoneNumber) {
        log.info("Попытка создания пользователя.");

        User user = User.builder()
                .username(username)
                .email(email)
                .phoneNumber(phoneNumber)
                .build();

        validateUserService.validateUserBeforeSaveForCreate(user);
        userRepository.save(user);

        BankAccount bankAccount = bankAccountService.createInitialAccount(user);
        user.setListBankAccounts(new ArrayList<>(List.of(bankAccount)));
        userRepository.save(user);

        log.info("Пользователь был создан. id: {}", user.getId());
        return user;
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return DTO пользователя
     */
    public UserResponseDto getUserByIdFromController(Long id) {
        log.info("Попытка получить пользователя по ID: {} в Rest контроллере", id);

        UserResponseDto dto = userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));

        log.info("Получен пользователь по ID: {}", id);
        return dto;
    }

    /**
     * Получает пользователя по идентификатору и статусу Active.
     *
     * @param id идентификатор пользователя
     * @return пользователь
     */
    public User getUserByIdAndStatusActive(Long id) {
        log.info("Попытка получить пользователя по ID: {} и статусом Active", id);

        User user = userRepository.findByIdAndStatus(id, UserStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));

        log.info("Получен пользователь. ID: {}", id);
        return user;
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return пользователь
     */
    public User getUserById(Long id) {
        log.info("Попытка получить пользователя по ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, id));

        log.info("Получен пользователь с ID: {}", id);
        return user;
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
     * Обновляет данные пользователя.
     *
     * @param id        идентификатор пользователя
     * @param updateDto DTO с обновленными данными
     * @return DTO обновленного пользователя
     */
    @Transactional
    public UserResponseDto updateUser(Long id, UpdateUserRequestDto updateDto) {
        log.info("Обновление пользователя с ID: {}", id);

        User user = getUserByIdAndStatusActive(id);
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

        validateUserService.validateUserBeforeSave(user);
        userRepository.save(user);

        log.info("Пользователь успешно сохранён с Id: {}.", user.getId());
    }
}