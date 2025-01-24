package kz.amihady.telegrambot.exception;

public class FeignClientException extends RuntimeException{

    public FeignClientException(String message) {
        super(message);
    }
}
