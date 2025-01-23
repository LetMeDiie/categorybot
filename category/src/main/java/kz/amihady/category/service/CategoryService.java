package kz.amihady.category.service;


import kz.amihady.category.dto.CategoryDto;
import kz.amihady.category.service.request.CreateCategoryRequest;
import kz.amihady.category.service.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getCategoryTree(Long userId);  // Получение всех категории что бы отправить другому сервису который занимается обработкой типа

    CategoryResponse addRootCategory(Long userID, CreateCategoryRequest request);  // Добавление корневой категории
    CategoryResponse addChildCategory(Long userId,String parentCategoryName , CreateCategoryRequest request);  // Добавление дочерней категории
    CategoryResponse removeCategory(Long userId,String categoryName);  // Удаление категории
}
