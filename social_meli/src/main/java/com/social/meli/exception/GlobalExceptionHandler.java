package com.social.meli.exception;

import com.social.meli.exception.user.EmailAlreadyExistsException;
import com.social.meli.exception.user.InvalidPasswordException;
import com.social.meli.exception.user.NicknameAlreadyExistsException;
import com.social.meli.exception.user.NicknameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleEmailExists(EmailAlreadyExistsException emailEx) {
        return emailEx.getMessage();
    }

    @ExceptionHandler(InvalidPasswordException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleInvalidPassword(InvalidPasswordException passwordEx) {
        return passwordEx.getMessage();
    }

    @ExceptionHandler(NicknameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)

    public String handleNicknameExists(NicknameAlreadyExistsException nicknameEx) {
        return nicknameEx.getMessage();
    }
    @ExceptionHandler(NicknameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)

    public String handleNicknameNotFound(NicknameNotFoundException nicknameNotEx) {
        return nicknameNotEx.getMessage();
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneric(Exception ex) {
        return "Erro inesperado: " + ex.getMessage();
}

}
