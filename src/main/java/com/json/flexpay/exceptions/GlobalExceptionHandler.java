package com.json.flexpay.exceptions;

import com.json.flexpay.model.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleBadRequestException(BadRequestException exception) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        errorResponse.setStatus("ERROR");
        errorResponse.setStatusCode("400");
        errorResponse.setMessage(exception.getMessage());
        return errorResponse;
    }


    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse handleNotFoundException(NotFoundException exception) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        errorResponse.setStatus("ERROR");
        errorResponse.setStatusCode("404");
        errorResponse.setMessage(exception.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrorResponse handleUnauthorizedException(AuthenticationException exception) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        errorResponse.setStatus("ERROR");
        errorResponse.setStatusCode("400");
        errorResponse.setMessage(exception.getMessage());
        return errorResponse;
    }

    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse handleInsufficientFundsException(InsufficientFundsException exception) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        errorResponse.setStatus("ERROR");
        errorResponse.setStatusCode("400");
        errorResponse.setMessage(exception.getMessage());
        return errorResponse;
    }
    @ExceptionHandler( UnauthorizedOperationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrorResponse handleUnauthorizedOperationException(UnauthorizedOperationException exception) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        errorResponse.setStatus("ERROR");
        errorResponse.setStatusCode("401");
        errorResponse.setMessage(exception.getMessage());
        return errorResponse;
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException exception) {
        ApiErrorResponse errorResponse = new ApiErrorResponse();
        errorResponse.setStatus("ERROR");
        errorResponse.setStatusCode("409");
        errorResponse.setMessage(exception.getMessage());
        return errorResponse;
    }

}

