package kz.amihady.categorytree.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import kz.amihady.categorytree.exception.CategoriesEmptyException;
import kz.amihady.categorytree.service.response.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;

public class CustomFeignErrorDecoder implements ErrorDecoder {
    private final ObjectMapper objectMapper;

    public CustomFeignErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            // Чтение тела ответа
            InputStream body = response.body().asInputStream();
            ErrorResponse errorResponse = objectMapper.readValue(body, ErrorResponse.class);

            if (response.status() == HttpStatus.NOT_FOUND.value()) {
                // Обрабатываем 404 (Not Found)
                return new CategoriesEmptyException(errorResponse.errorMessage());
            }
        } catch (IOException e) {
            // Обработка ошибок при чтении тела
            return new Exception("Ошибка при чтение тела: " + e.getMessage());
        }
        return FeignException.errorStatus(methodKey, response);
    }
}