package kz.amihady.categorytree.handler;


import kz.amihady.categorytree.exception.CategoriesEmptyException;
import kz.amihady.categorytree.exception.CategoryTreeConversionFileException;
import kz.amihady.categorytree.service.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class CategoryTreeExceptionHandler {

    @ExceptionHandler(CategoryTreeConversionFileException.class)
    public ResponseEntity<ErrorResponse> handleCategoryTreeConversionFileException(CategoryTreeConversionFileException exception) {
        log.error("Ошибка при обработке файла Category Tree: {}", exception.getMessage(), exception);

        ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(CategoriesEmptyException.class)
    public ResponseEntity<ErrorResponse> handleCategoriesEmptyException(CategoriesEmptyException ex){
        log.error("Список для клиента пустой");
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}

