package gigabank.accountmanagement.utility.validators;

import gigabank.accountmanagement.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ValidateAccountService {
    private final AccountRepository accountRepository;


}
