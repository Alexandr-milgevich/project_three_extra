package com.gigabank.utility.validators;

import com.gigabank.constants.status.UserStatus;
import com.gigabank.exceptions.User.UserAlreadyExistsException;
import com.gigabank.exceptions.User.UserValidationException;
import com.gigabank.models.entity.BankAccount;
import com.gigabank.models.entity.User;
import com.gigabank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.gigabank.utility.Utility.isFilled;

/**
 * Сервис для валидации данных пользователя перед сохранением в базу данных.
 * Выполняет проверки:
 * - обязательных полей на заполненность;
 * - корректности форматов данных;
 * - уникальности email и номера телефона.
 * При обнаружении ошибок выбрасывает соответствующие исключения.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ValidateUserService {
    private final UserRepository userRepository;

    /**
     * Проверяет все основные данные пользователя перед сохранением.
     * В случае некорректных данных выбрасывает исключения валидации.
     *
     * @param user объект пользователя для валидации
     */
    public void validateDataBeforeSave(User user) {
        checkUserOnNull(user);
        checkEmail(user.getEmail());
        checkPhoneNumber(user.getPhoneNumber());
        checkUsername(user.getUsername());
        checkListAccounts(user.getListBankAccounts());
    }

    /**
     * Проверяет уникальность email и номера телефона пользователя.
     * Если email или номер телефона уже существуют — выбрасывает соответствующие исключения.
     *
     * @param email       email для проверки
     * @param phoneNumber номер телефона для проверки
     */
    public void checkEmailAndPhoneUniqueness(String email, String phoneNumber) {
        checkEmailUniqueness(email);
        checkPhoneNumberUniqueness(phoneNumber);
    }

    /**
     * Проверяет два статуса счета: старый и изменяемый.
     *
     * @param newStatus статус для изменения
     * @param oldStatus первоначальный статус
     */
    public void checkUserStatus(UserStatus newStatus, UserStatus oldStatus) {
        if (UserStatus.isValid(newStatus.name()))
            throw new UserValidationException("Недопустимый статус: " + newStatus.name());
        if (oldStatus == newStatus)
            throw new UserValidationException("Пользователь уже имеет статус: " + newStatus);
    }

    /**
     * Проверяет, что объект пользователя не равен null.
     * Если равен — выбрасывает исключение валидации.
     *
     * @param user объект пользователя
     */
    private void checkUserOnNull(User user) {
        if (Objects.isNull(user)) throw new UserValidationException("Пользователя не существует!");
    }

    /**
     * Проверяет, что email заполнен.
     * Если пустой или null — выбрасывает исключение.
     *
     * @param email email пользователя
     */
    private void checkEmail(String email) {
        if (!isFilled(email)) throw new UserValidationException("Не указана эл. почта");
    }

    /**
     * Проверяет, что номер телефона заполнен.
     * Если пустой или null — выбрасывает исключение.
     *
     * @param phoneNumber номер телефона пользователя
     */
    private void checkPhoneNumber(String phoneNumber) {
        if (!isFilled(phoneNumber)) throw new UserValidationException("Не указан номер телефона");
    }

    /**
     * Проверяет, что фамилия заполнена.
     * Если пустая или null — выбрасывает исключение.
     *
     * @param lastName фамилия пользователя
     */
    private void checkUsername(String lastName) {
        if (!isFilled(lastName)) throw new UserValidationException("Не указано имя пользователя");
    }

    /**
     * Проверяет уникальность номера телефона.
     * Если номер уже существует — выбрасывает исключение.
     *
     * @param phoneNumber номер телефона пользователя
     */
    private void checkPhoneNumberUniqueness(String phoneNumber) {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            log.warn("Пользователь с телефоном {} уже существует", phoneNumber);
            throw new UserAlreadyExistsException("Пользователь с таким телефоном уже существует");
        }
    }

    /**
     * Проверяет уникальность email.
     * Если email уже существует — выбрасывает исключение.
     *
     * @param email email пользователя
     */
    private void checkEmailUniqueness(String email) {
        if (userRepository.existsByEmail(email)) {
            log.warn("Пользователь с email {} уже существует", email);
            throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
        }
    }

    /**
     * Проверяет, что список счетов пользователя не пустой.
     * Если список пуст или null — выбрасывает исключение.
     *
     * @param listBankAccounts список счетов пользователя
     */
    private void checkListAccounts(List<BankAccount> listBankAccounts) {
        if (listBankAccounts == null || listBankAccounts.isEmpty())
            throw new UserValidationException("У пользователя отсутствуют счета");
    }
}