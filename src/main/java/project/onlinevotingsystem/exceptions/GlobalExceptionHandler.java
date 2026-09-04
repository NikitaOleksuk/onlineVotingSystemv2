package project.onlinevotingsystem.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AppError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity.badRequest()
                .body(new AppError(HttpStatus.BAD_REQUEST.value(), "Помилка валідації: " + details));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<AppError> handleConstraintViolation(ConstraintViolationException ex) {
        String details = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));

        return ResponseEntity.badRequest()
                .body(new AppError(HttpStatus.BAD_REQUEST.value(), "Помилка валідації: " + details));
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<AppError> handleNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(
                new AppError(HttpStatus.BAD_REQUEST.value(), "Неправильний формат запиту")
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<AppError> handleValidationException(IllegalArgumentException e) {
        return new ResponseEntity<>(
                new AppError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<AppError> handleBadCredentials(BadCredentialsException e) {
        return new ResponseEntity<>(
                new AppError(HttpStatus.UNAUTHORIZED.value(), "Невірний логін або пароль"),
                HttpStatus.UNAUTHORIZED
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<AppError> handleUserNotFound(UsernameNotFoundException e) {
        return new ResponseEntity<>(
                new AppError(HttpStatus.NOT_FOUND.value(), e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<AppError> handleEntityNotFound( EntityNotFoundException e){
        return  new ResponseEntity<>(
                new AppError(HttpStatus.NO_CONTENT.value(),e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<AppError> handleIllegalState(IllegalStateException e) {
        return new ResponseEntity<>(
                new AppError(HttpStatus.BAD_REQUEST.value(), e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }
    @ExceptionHandler(InvalidDataAccessResourceUsageException.class)
    public ResponseEntity<AppError> handleDatabaseError(InvalidDataAccessResourceUsageException e) {
        return new ResponseEntity<>(
                new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Помилка бази даних"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<AppError> handleDataAccessError(DataAccessException e) {
        return new ResponseEntity<>(
                new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Помилка доступу до даних"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<AppError> handleGeneral(Exception e) {
        return new ResponseEntity<>(
                new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Внутрішня помилка сервера"),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
