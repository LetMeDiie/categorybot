package kz.amihady.telegrambot.feign.fallback;

import kz.amihady.telegrambot.feign.CategoryTreeServiceClient;
import kz.amihady.telegrambot.feign.response.CategoryTreeExelResponse;
import kz.amihady.telegrambot.feign.response.CategoryTreeStringResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryTreeServiceFallBack implements CategoryTreeServiceClient {
    private final static String ERROR_MESSAGE =
            "Сервис category-tree недоступен , попробуйте позже.";;

    @Override
    public CategoryTreeStringResponse getTreeAsString(Long userId) {
        throw  new RuntimeException(ERROR_MESSAGE);
    }

    @Override
    public CategoryTreeExelResponse getTreeAsFile(Long userId) {
        throw new RuntimeException(ERROR_MESSAGE);
    }
}
