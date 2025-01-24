package kz.amihady.telegrambot.feign;


import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import kz.amihady.telegrambot.exception.FeignClientException;
import kz.amihady.telegrambot.feign.response.ErrorResponse;

import java.io.IOException;

public class FeignErrorDecoder implements ErrorDecoder {
//каждая ошибка в сервисе возвращается ввиде errorResponse string errorMessage
    //их обработка выглядит одинаково
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            if (response.body() != null) {
                ErrorResponse errorResponse =
                        objectMapper.readValue(response.body().asInputStream(), ErrorResponse.class);

                String errorMessage = errorResponse.errorMessage() != null
                        ? errorResponse.errorMessage()
                        : "Неизвестная ошибка в системе";

                return new FeignClientException(errorMessage);
            }
        } catch (IOException e) {
            return new FeignClientException("Ошибка при чтении ответа от сервиса");
        }

        return new FeignClientException("Неизвестная ошибка в системе");
    }
}