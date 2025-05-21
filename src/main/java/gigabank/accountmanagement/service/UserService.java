package gigabank.accountmanagement.service;

import gigabank.accountmanagement.exceptions.UserException;
import gigabank.accountmanagement.models.entity.User;
import gigabank.accountmanagement.repository.UserRepository;
import gigabank.accountmanagement.utility.validators.ValidateUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import static gigabank.accountmanagement.utility.Utility.isFilled;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ValidateUserService validateUserService;

    @Transactional
    public User create(String email, String lastName, String firstName, String middleName, String phoneNumber, LocalDate birthDate) throws UserException {
        validateUserService.validateUserData(email, lastName, firstName, phoneNumber, birthDate);

        return User.builder()
                .uuid(UUID.randomUUID().toString())
                .email(email)
                .lastName(lastName)
                .firstName(firstName)
                .middleName(middleName)
                .phoneNumber(phoneNumber)
                .birthDate(birthDate)
                .build();
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User getByPhone(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }

    public User getByUuid(String uuid) {
        return userRepository.findByUuid(uuid).orElse(null);
    }

    @Transactional
    public User save(User user) throws UserException {
        if (Objects.isNull(user)) throw new UserException("Пользователя не существует!");

        validateUserService.validateUserData(user.getEmail(), user.getLastName(),
                user.getFirstName(), user.getPhoneNumber(),
                user.getBirthDate());

        if (Objects.isNull(user.getListAccounts()) || user.getListAccounts().isEmpty())
            throw new UserException("У пользователя отсутствуют счета");

        return userRepository.save(user);
    }

    @Transactional
    public void deleteByUuid(String uuid) throws UserException {
        if (!isFilled(uuid)) {
            log.error("Попытка удаления пользователя с пустым UUID");
            throw new UserException("UUID не может быть пустым");
        }
        if (userRepository.deleteByUuid(uuid) == 0) {
            log.warn("Пользователь с UUID {} не найден", uuid);
            throw new UserException("Пользователь не найден с UUID: " + uuid);
        }
        log.info("Пользователь с UUID {} удален", uuid);
    }

    @Transactional
    public void deleteByEmail(String email) throws UserException {
        if (!isFilled(email)) {
            log.error("Попытка удаления пользователя с пустым email");
            throw new UserException("Email не может быть пустым");
        }
        if (userRepository.deleteByEmail(email) == 0) {
            log.warn("Пользователь с email {} не найден", email);
            throw new UserException("Пользователь не найден с UUID: " + email);
        }
        log.info("Пользователь с email {} удален", email);
    }
}