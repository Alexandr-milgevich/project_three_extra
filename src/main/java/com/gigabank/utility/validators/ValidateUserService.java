package com.gigabank.utility.validators;

import com.gigabank.exceptions.buisnes_logic.EntityExistsException;
import com.gigabank.exceptions.buisnes_logic.EntityValidationException;
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
        checkId(user.getId());
        checkEmail(user.getEmail());
        checkPhoneNumber(user.getPhoneNumber());
        checkUsername(user.getUsername());
        checkListAccounts(user.getListBankAccounts());
    }

    public void validateUserBeforeSaveForCreate(User user) {
        checkEmailAndPhoneUniqueness(user.getEmail(), user.getPhoneNumber());
        checkUsername(user.getUsername());
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
     * Проверяет, что пользователь по данному идентификатору существует
     *
     * @param id идентификатор пользователя
     */
    private void checkId(Long id) {
        if (Objects.isNull(id) || !userRepository.existsById(id))
            throw new EntityValidationException(User.class, "У пользователя должен быть идентификатор");
    }

    /**
     * Проверяет, что объект пользователя не равен null.
     * Если равен — выбрасывает исключение валидации.
     *
     * @param user объект пользователя
     */
    private void checkUserOnNull(User user) {
        if (Objects.isNull(user)) throw new EntityValidationException(User.class, "Пользователя не существует!");
    }

    /**
     * Проверяет, что email заполнен.
     * Если пустой или null — выбрасывает исключение.
     *
     * @param email email пользователя
     */
    private void checkEmail(String email) {
        if (!isFilled(email)) throw new EntityValidationException(User.class, "Не указана эл. почта");
    }

    /**
     * Проверяет, что номер телефона заполнен.
     * Если пустой или null — выбрасывает исключение.
     *
     * @param phoneNumber номер телефона пользователя
     */
    private void checkPhoneNumber(String phoneNumber) {
        if (!isFilled(phoneNumber)) throw new EntityValidationException(User.class, "Не указан номер телефона");
    }

    /**
     * Проверяет, что фамилия заполнена.
     * Если пустая или null — выбрасывает исключение.
     *
     * @param lastName фамилия пользователя
     */
    private void checkUsername(String lastName) {
        if (!isFilled(lastName)) throw new EntityValidationException(User.class, "Не указано имя пользователя");
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
            throw new EntityExistsException(User.class, "Пользователь с таким телефоном уже существует");
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
            throw new EntityExistsException(User.class, "Пользователь с таким email уже существует");
        }
    }

    /**
     * Проверяет, что список счетов пользователя не пустой.
     * Если список пуст или null — выбрасывает исключение.
     *
     * @param listBankAccounts список счетов пользователя
     */
    private void checkListAccounts(List<BankAccount> listBankAccounts) {
        if (Objects.isNull(listBankAccounts) || listBankAccounts.isEmpty())
            throw new EntityValidationException(User.class, "У пользователя отсутствуют счета");
    }
}