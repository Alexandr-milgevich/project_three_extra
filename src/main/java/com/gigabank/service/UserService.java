package com.gigabank.service;

import com.gigabank.exceptions.UserException;
import com.gigabank.models.entity.User;
import com.gigabank.repository.UserRepository;
import com.gigabank.utility.validators.ValidateUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

import static com.gigabank.utility.Utility.isFilled;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ValidateUserService validateUserService;

    @Transactional
    public void create(String email, String lastName, String firstName, String middleName, String phoneNumber, LocalDate birthDate) throws UserException {
        validateUserService.validateUserData(email, lastName, firstName, phoneNumber, birthDate);

        User user = User.builder()
                .email(email)
                .lastName(lastName)
                .firstName(firstName)
                .middleName(middleName)
                .phoneNumber(phoneNumber)
                .birthDate(birthDate)
                .build();

        log.info("Пользователь с id {} был создан.", user.getId());
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User getByPhone(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }

    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(User user) throws UserException {
        if (Objects.isNull(user)) throw new UserException("Пользователя не существует!");

        validateUserService.validateUserData(user.getEmail(), user.getLastName(),
                user.getFirstName(), user.getPhoneNumber(),
                user.getBirthDate());

        if (Objects.isNull(user.getListAccounts()) || user.getListAccounts().isEmpty())
            throw new UserException("У пользователя отсутствуют счета");

        log.info("Пользователь с UUID {} был сохранен в БД.", user.getId());
        userRepository.save(user);
    }

    @Transactional
    public void deleteById(Long id) throws UserException {
        if (!isFilled(id)) {
            log.error("Попытка удаления пользователя с пустым id");
            throw new UserException("Id не может быть пустым");
        }
        if (userRepository.deleteById(id) == 0) {
            log.warn("Пользователь с id {} не найден", id);
            throw new UserException("Пользователь не найден с id: " + id);
        }
        log.info("Пользователь с идентификатором {} удален", id);
    }

    @Transactional
    public void deleteByEmail(String email) throws UserException {
        if (!isFilled(email)) {
            log.error("Попытка удаления пользователя с пустым email");
            throw new UserException("Email не может быть пустым");
        }
        if (userRepository.deleteByEmail(email) == 0) {
            log.warn("Пользователь с email {} не найден", email);
            throw new UserException("Пользователь не найден с email: " + email);
        }
        log.info("Пользователь с email {} удален", email);
    }
}