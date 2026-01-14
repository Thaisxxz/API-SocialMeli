package com.social.meli.exception;

import com.social.meli.exception.follower.BusinessException;
import com.social.meli.exception.follower.FollowerNotFoundException;
import com.social.meli.exception.follower.UnauthorizedException;
import com.social.meli.exception.order.InvalidOrderException;
import com.social.meli.exception.post.PostNotFoundException;
import com.social.meli.exception.product.ProductNotFoundException;
import com.social.meli.exception.product.category.CategoryNotFoundException;
import com.social.meli.exception.profile.ProfileNotFoundException;
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

    @ExceptionHandler(ProfileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleProfileNotFound(ProfileNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(FollowerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleFollowerNotFound(FollowerNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleUnauthorized(UnauthorizedException followerEx) {
        return followerEx.getMessage();
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public String handleBusinessException(BusinessException businessEx) {
        return businessEx.getMessage();
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleProductNotFound(ProductNotFoundException productEx){
        return productEx.getMessage();
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleCategoryNotFound(CategoryNotFoundException categoryEx){
        return categoryEx.getMessage();
    }

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlePostNotFound(PostNotFoundException postEx){
        return postEx.getMessage();
    }

    @ExceptionHandler(InvalidOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidOrder(InvalidOrderException ex) {
        return ex.getMessage();
    }
}