package com.gigabank.utility.validators;

import com.gigabank.exceptions.User.UserAlreadyExistsException;
import com.gigabank.exceptions.User.UserValidationException;
import com.gigabank.models.entity.User;
import com.gigabank.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

import static com.gigabank.utility.Utility.isFilled;

/**
 * Сервис для валидации данных пользователя перед сохранением в базу данных.
 * Выполняет проверки:
 * -) Наполненности обязательных полей;
 * -) Корректности форматов данных;
 * -) Уникальности email и номера телефона.
 * При обнаружении ошибок выбрасывает соответствующие исключения.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ValidateUserService {
    private final UserRepository userRepository;

    /**
     * Проверяет все основные данные пользователя.
     *
     * @param user объект пользователя для валидации
     * @throws UserValidationException если какое-либо из обязательных полей не прошло проверку
     */
    public void validateDataBeforeSave(User user) {
        checkEmail(user.getEmail());
        checkPhoneNumber(user.getPhoneNumber());
        checkLastName(user.getLastName());
        checkFirstName(user.getFirstName());
        checkBirthDate(user.getBirthDate());
    }

    /**
     * Проверяет email пользователя.
     *
     * @param email email для проверки
     * @throws UserValidationException если email пустой или null
     */
    private void checkEmail(String email) {
        if (!isFilled(email)) {
            throw new UserValidationException("Не указана эл. почта");
        }
    }

    /**
     * Проверяет номер телефона пользователя.
     *
     * @param phoneNumber номер телефона для проверки
     * @throws UserValidationException если номер телефона пустой или null
     */
    private void checkPhoneNumber(String phoneNumber) {
        if (!isFilled(phoneNumber)) {
            throw new UserValidationException("Не указан номер телефона");
        }
    }

    /**
     * Проверяет фамилию пользователя.
     *
     * @param lastName фамилия для проверки
     * @throws UserValidationException если фамилия пустая или null
     */
    private void checkLastName(String lastName) {
        if (!isFilled(lastName)) {
            throw new UserValidationException("Не указана фамилия");
        }
    }

    /**
     * Проверяет имя пользователя.
     *
     * @param firstName имя для проверки
     * @throws UserValidationException если имя пустое или null
     */
    private void checkFirstName(String firstName) {
        if (!isFilled(firstName)) {
            throw new UserValidationException("Не указано имя");
        }
    }

    /**
     * Проверяет дату рождения пользователя.
     *
     * @param birthDate дата рождения для проверки
     * @throws UserValidationException если: дата рождения не указана или передана в будущем формате
     */
    private void checkBirthDate(LocalDate birthDate) {
        if (Objects.isNull(birthDate)) {
            throw new UserValidationException("Дата рождения не указана.");
        }
        if (birthDate.isAfter(LocalDate.now())) {
            throw new UserValidationException("Дата рождения указана неверно.");
        }
    }

    /**
     * Проверяет уникальность email и номера телефона пользователя.
     *
     * @param email       email для проверки
     * @param phoneNumber номер телефона для проверки
     * @throws UserAlreadyExistsException если email или номер телефона уже существуют в системе
     */
    public void checkEmailAndPhoneUniqueness(String email, String phoneNumber) {
        checkEmailUniqueness(email);
        checkPhoneNumberUniqueness(phoneNumber);
    }

    /**
     * Проверяет уникальность номера телефона в системе.
     *
     * @param phoneNumber номер телефона для проверки
     * @throws UserAlreadyExistsException если номер телефона уже зарегистрирован
     */
    private void checkPhoneNumberUniqueness(String phoneNumber) {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            log.warn("Пользователь с телефоном {} уже существует", phoneNumber);
            throw new UserAlreadyExistsException("Пользователь с таким телефоном уже существует");
        }
    }

    /**
     * Проверяет уникальность email в системе.
     *
     * @param email email для проверки
     * @throws UserAlreadyExistsException если email уже зарегистрирован
     */
    private void checkEmailUniqueness(String email) {
        if (userRepository.existsByEmail(email)) {
            log.warn("Пользователь с email {} уже существует", email);
            throw new UserAlreadyExistsException("Пользователь с таким email уже существует");
        }
    }
}