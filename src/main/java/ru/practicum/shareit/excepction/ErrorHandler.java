package ru.practicum.shareit.excepction;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

    @Slf4j
    @RestControllerAdvice
    public class ErrorHandler {

        @ExceptionHandler
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public ErrorResponse handleValidationException(final MethodArgumentNotValidException e) {
            log.warn(e.getMessage(), e);
            return new ErrorResponse(e.getMessage());
        }

        @ExceptionHandler
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public ErrorResponse handleNotOwner(final NotOwnerException e) {
            log.warn(e.getMessage());
            return new ErrorResponse(e.getMessage());
        }

        @ExceptionHandler
        @ResponseStatus(HttpStatus.CONFLICT)
        public ErrorResponse handleDuplicateException(final DuplicateException e) {
            log.warn(e.getMessage());
            return new ErrorResponse(e.getMessage());
        }

        @ExceptionHandler
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public ErrorResponse handleObjectNotFoundException(final ObjectNotFoundException e) {
            log.warn(e.getMessage());
            return new ErrorResponse(e.getMessage());
        }

        @ExceptionHandler
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        public ErrorResponse handleThrowable(final Throwable e) {
            log.warn(e.getMessage(), e);
            return new ErrorResponse(e.getMessage());
        }
    }
