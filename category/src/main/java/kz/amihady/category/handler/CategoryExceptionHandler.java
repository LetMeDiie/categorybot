package kz.amihady.category.handler;


import kz.amihady.category.exception.CategoriesNotFoundException;
import kz.amihady.category.exception.CategoryAlreadyExistsException;
import kz.amihady.category.exception.CategoryNotFoundException;
import kz.amihady.category.service.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CategoryExceptionHandler {

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoryNotFoundException(CategoryNotFoundException ex) {
        log.error("Категория не найдена: {}", ex.getMessage(), ex);  // Логирование ошибки
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException ex) {
        log.error("Категория уже существует: {}", ex.getMessage(), ex);  // Логирование ошибки
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CategoriesNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCategoriesNotFoundException(CategoriesNotFoundException exception) {
        log.error("Категории не найдены: {}", exception.getMessage(), exception);  // Логирование ошибки
        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}