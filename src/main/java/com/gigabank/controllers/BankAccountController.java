package com.gigabank.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class BankAccountController {

/*
- POST /accounts — создать новый аккаунт
- GET /accounts/{id} — получить данные аккаунта
- POST /accounts/{id}/deposit — пополнить баланс
- POST /accounts/{id}/withdraw — снять средства
- GET /accounts/{id}/transactions — получить историю операций
 */


    /*
    private final RegistrationService registrationService;
    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> get(@RequestParam String login, String password, String secretKey) {
        try {
            return new ResponseEntity<>(userService.get(login, password, secretKey), OK);
        } catch (DataVerificationException e) {
            return new ResponseEntity<>(e.getMessage(), UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody AlainDelon alainDelon) {
        try {
            return new ResponseEntity<>(userService.save(alainDelon), OK);
        } catch (DataVerificationException e) {
            return new ResponseEntity<>(e.getMessage(), UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registrationViaApp(@RequestParam String login, String email, String password, String secretKey) {
        try {
            return new ResponseEntity<>(registrationService.simpleFormApp(login, email, password, secretKey), OK);
        } catch (DataVerificationException e) {
            return new ResponseEntity<>(e.getMessage(), UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }
     */
}
