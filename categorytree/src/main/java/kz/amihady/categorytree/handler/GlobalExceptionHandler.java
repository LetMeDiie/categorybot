package kz.amihady.categorytree.handler;


import feign.FeignException;
import kz.amihady.categorytree.service.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Обработка любых других необработанных исключений
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        log.error("Произошла неожиданная ошибка на стороне сервера: {}", ex.getMessage(), ex); // Логирование ошибки
        ErrorResponse errorResponse = new ErrorResponse("Произошла неожиданная ошибка на стороне сервера.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Обработка ошибок на стороне категории сервиса (FeignException)
    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(FeignException e) {
        log.error("Ошибка при обращении к удаленному сервису: {}. Код состояния: {}", e.getMessage(), e.status(), e); // Логирование ошибки
        ErrorResponse errorResponse = new ErrorResponse("Ошибка при обращении к удаленному сервису");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
