package gigabank.accountmanagement.utility.validators;

import gigabank.accountmanagement.exceptions.UserException;
import gigabank.accountmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;

import static gigabank.accountmanagement.utility.Utility.isFilled;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidateUserService {
    private final UserRepository userRepository;

    public void validateUserData(String email, String lastName,
                                 String firstName, String phoneNumber,
                                 LocalDate birthDate) throws UserException {
        checkEmail(email);
        checkPhoneNumber(phoneNumber);
        checkLastName(lastName);
        checkFirstName(firstName);
        checkBirthDate(birthDate);

        checkEmailAndPhoneUniqueness(email, phoneNumber);
        // Можно добавить проверку формата email и телефона
    }

    private void checkEmail(String email) throws UserException {
        if (!isFilled(email)) throw new UserException("Не указана эл. почта");
    }

    private void checkPhoneNumber(String phoneNumber) throws UserException {
        if (!isFilled(phoneNumber)) throw new UserException("Не указан номер телефона");
    }

    private void checkLastName(String lastName) throws UserException {
        if (!isFilled(lastName)) throw new UserException("Не указана фамилия");
    }

    private void checkFirstName(String firstName) throws UserException {
        if (!isFilled(firstName)) throw new UserException("Не указано имя");
    }

    private void checkBirthDate(LocalDate birthDate) throws UserException {

        if (Objects.isNull(birthDate)) throw new UserException("Дата рождения не указана.");
        if (birthDate.isAfter(LocalDate.now())) throw new UserException("Дата рождения указана неверно.");
    }

    private void checkEmailAndPhoneUniqueness(String email, String phoneNumber) throws UserException {
        checkEmailUniqueness(email);
        checkPhoneNumberUniqueness(phoneNumber);
    }

    private void checkPhoneNumberUniqueness(String phoneNumber) throws UserException {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            log.warn("Пользователь с телефоном {} уже существует", phoneNumber);
            throw new UserException("Пользователь с таким телефоном уже существует");
        }
    }

    private void checkEmailUniqueness(String email) throws UserException {
        if (userRepository.existsByEmail(email)) {
            log.warn("Пользователь с email {} уже существует", email);
            throw new UserException("Пользователь с таким email уже существует");
        }
    }
}