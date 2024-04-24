package ua.vahskulat.exception.handler;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.vahskulat.dto.response.ErrorResponseAPI;
import ua.vahskulat.dto.response.ErrorTitle;
import ua.vahskulat.exception.UserEmailExistException;
import ua.vahskulat.exception.UserNotFoundException;
import ua.vahskulat.exception.UserWrongAgeException;
import ua.vahskulat.exception.UserWrongDateException;

import java.time.LocalDateTime;
import java.util.Objects;

@RestControllerAdvice
public class UserControllerAdvice {
    @ExceptionHandler(UserWrongDateException.class)
    public ResponseEntity<ErrorResponseAPI> handleUserWrongDateException(UserWrongDateException exception
            , HttpServletRequest request) {

        return ResponseEntity.badRequest()
                .body(new ErrorResponseAPI(
                        ErrorTitle.WRONG_DATE.getError()
                        ,exception.getMessage()
                        ,request.getRequestURI()
                        , LocalDateTime.now()
                ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseAPI> handleUserNotFoundException(UserNotFoundException exception
            , HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseAPI(
                        ErrorTitle.USER_NOT_FOUND.getError()
                        ,exception.getMessage()
                        ,request.getRequestURI()
                        , LocalDateTime.now()
                ));


    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseAPI> handleIllegalArgumentException(ConstraintViolationException exception
            , HttpServletRequest request) {

        return ResponseEntity.badRequest()
                .body(new ErrorResponseAPI(
                        ErrorTitle.INVALID_QUERY_PARAMETER.getError()
                        ,exception.getMessage()
                        ,request.getRequestURI()
                        , LocalDateTime.now()
                ));

    }

    @ExceptionHandler(UserWrongAgeException.class)
    public ResponseEntity<ErrorResponseAPI> handleUserWrongAgeException(UserWrongAgeException exception
            , HttpServletRequest request) {


        return ResponseEntity.status(HttpStatus.BAD_REQUEST)//HttpStatus.UNPROCESSABLE_ENTITY
                .body(new ErrorResponseAPI(
                        ErrorTitle.WRONG_AGE.getError()
                        ,exception.getMessage()
                        ,request.getRequestURI()
                        , LocalDateTime.now()
                ));

    }

    @ExceptionHandler(UserEmailExistException.class)
    public ResponseEntity<ErrorResponseAPI> handleUserEmailExistException(UserEmailExistException exception
            , HttpServletRequest request) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponseAPI(
                        ErrorTitle.USER_EMAIL_EXIST.getError()
                        ,exception.getMessage()
                        ,request.getRequestURI()
                        , LocalDateTime.now()
                ));

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)

    public ResponseEntity<ErrorResponseAPI> handlerMethodArgumentNotValidException(MethodArgumentNotValidException exception
            , HttpServletRequest request) {
        String detail = Objects.requireNonNull(exception.getBindingResult().getFieldError().getDefaultMessage());
        return ResponseEntity.badRequest()
                .body(new ErrorResponseAPI(
                        ErrorTitle.VALIDATION_ERROR.getError()
                        ,exception.getMessage()
                        ,request.getRequestURI()
                        , LocalDateTime.now()
                ));
    }
}
