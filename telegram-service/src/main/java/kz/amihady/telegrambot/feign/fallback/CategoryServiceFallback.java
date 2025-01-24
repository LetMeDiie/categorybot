package kz.amihady.telegrambot.feign.fallback;

import kz.amihady.telegrambot.feign.CategoryServiceClient;
import kz.amihady.telegrambot.feign.request.CreateNewCategoryRequest;
import kz.amihady.telegrambot.feign.response.CategoryResponse;
import org.springframework.stereotype.Component;


// в случае если сервис недоступен , будут выполняться эти методы.
@Component
public class CategoryServiceFallback implements CategoryServiceClient {
    private final static String ERROR_MESSAGE =
            "Сервис категорий недоступен , попробуйте позже.";

    @Override
    public CategoryResponse addRootCategory(Long userId, CreateNewCategoryRequest request) {
        throw new RuntimeException(ERROR_MESSAGE);
    }

    @Override
    public CategoryResponse addChildCategory(Long userId, String parentCategoryName, CreateNewCategoryRequest request) {
        throw new RuntimeException(ERROR_MESSAGE);
    }

    @Override
    public CategoryResponse deleteCategory(Long userId, String categoryName) {
        throw new RuntimeException(ERROR_MESSAGE);
    }
}

