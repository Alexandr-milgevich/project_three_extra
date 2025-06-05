package com.gigabank.service;

import com.gigabank.exceptions.User.UserAlreadyExistsException;
import com.gigabank.exceptions.User.UserNotFoundException;
import com.gigabank.exceptions.User.UserValidationException;
import com.gigabank.mappers.UserMapper;
import com.gigabank.models.dto.request.user.UserRequestDto;
import com.gigabank.models.dto.request.user.UserUpdateRequestDto;
import com.gigabank.models.dto.response.UserResponseDto;
import com.gigabank.models.entity.User;
import com.gigabank.repository.UserRepository;
import com.gigabank.utility.validators.ValidateUserBeforeSave;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.gigabank.utility.Utility.isFilled;

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
    private final ValidateUserBeforeSave validateUserBeforeSave;

    /**
     * Создает нового пользователя в системе.
     *
     * @param requestDto DTO с данными для создания пользователя
     * @return DTO созданного пользователя
     * @throws UserAlreadyExistsException если пользователь с таким email или телефоном уже существует
     * @throws UserValidationException если данные пользователя не прошли валидацию
     */
    @Transactional
    public UserResponseDto createUser(UserRequestDto requestDto) {
        User user = userMapper.toEntity(requestDto);

        validateUserBeforeSave.validateUserData(user);
        validateUserBeforeSave.checkEmailAndPhoneUniqueness(user.getEmail(), user.getPhoneNumber());

        User savedUser = userRepository.save(user);
        log.info("Пользователь с id {} был создан.", savedUser.getId());
        return userMapper.toDto(savedUser);
    }

    /**
     * Получает пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @return DTO пользователя
     * @throws UserNotFoundException если пользователь не найден
     */
    public UserResponseDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден по id: " + id));
    }

    /**
     * Получает пользователя по email.
     *
     * @param email email пользователя
     * @return DTO пользователя
     * @throws UserNotFoundException если пользователь не найден
     */
    public UserResponseDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден по email: " + email));
    }

    /**
     * Получает пользователя по номеру телефона.
     *
     * @param phoneNumber номер телефона пользователя
     * @return DTO пользователя
     * @throws UserNotFoundException если пользователь не найден
     */
    public UserResponseDto getUserByPhone(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден по номеру телефона: " + phoneNumber));
    }

    /**
     * Обновляет данные пользователя.
     *
     * @param id идентификатор пользователя
     * @param updateDto DTO с обновленными данными
     * @return DTO обновленного пользователя
     * @throws UserNotFoundException если пользователь не найден
     * @throws UserValidationException если данные не прошли валидацию
     */
    @Transactional
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto updateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден по id: " + id));

        userMapper.updateFromDto(updateDto, user);
        validateUserBeforeSave.validateUserData(user);

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    /**
     * Сохраняет пользователя в базу данных.
     *
     * @param user сущность пользователя
     * @throws UserValidationException если пользователь не валиден или отсутствуют счета
     */
    @Transactional
    public void save(User user) {
        if (Objects.isNull(user)) {
            throw new UserValidationException("Пользователя не существует!");
        }
        validateUserBeforeSave.validateUserData(user);
        if (Objects.isNull(user.getListAccounts()) || user.getListAccounts().isEmpty()) {
            throw new UserValidationException("У пользователя отсутствуют счета");
        }

        log.info("Пользователь с UUID {} был сохранен в БД.", user.getId());
        userRepository.save(user);
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id идентификатор пользователя
     * @throws UserValidationException если id равен null
     * @throws UserNotFoundException если пользователь не найден
     */
    @Transactional
    public void deleteUserById(Long id) {
        if (id == null) {
            log.error("Попытка удаления пользователя с пустым id");
            throw new UserValidationException("Id не может быть пустым");
        }
        if (userRepository.deleteById(id) == 0) {
            log.warn("Пользователь с id {} не найден", id);
            throw new UserNotFoundException("Пользователь не найден с id: " + id);
        }
        log.info("Пользователь с идентификатором {} удален", id);
    }

    /**
     * Удаляет пользователя по email.
     *
     * @param email email пользователя
     * @throws UserValidationException если email пустой
     * @throws UserNotFoundException если пользователь не найден
     */
    @Transactional
    public void deleteUserByEmail(String email) {
        if (!isFilled(email)) {
            log.error("Попытка удаления пользователя с пустым email");
            throw new UserValidationException("Email не может быть пустым");
        }
        if (userRepository.deleteByEmail(email) == 0) {
            log.warn("Пользователь с email {} не найден", email);
            throw new UserNotFoundException("Пользователь не найден с email: " + email);
        }
        log.info("Пользователь с email {} удален", email);
    }

    /**
     * Удаляет пользователя по номеру телефона.
     *
     * @param phoneNumber номер телефона пользователя
     * @throws UserValidationException если номер телефона пустой
     * @throws UserNotFoundException если пользователь не найден
     */
    @Transactional
    public void deleteUserByPhoneNumber(String phoneNumber) {
        if (!isFilled(phoneNumber)) {
            log.error("Попытка удаления пользователя с пустым номером телефона");
            throw new UserValidationException("Номер телефона не может быть пустым");
        }
        if (userRepository.deleteByPhoneNumber(phoneNumber) == 0) {
            log.warn("Пользователь с номером телефона {} не найден", phoneNumber);
            throw new UserNotFoundException("Пользователь не найден с номером телефона: " + phoneNumber);
        }
        log.info("Пользователь с номером телефона {} удален", phoneNumber);
    }
}