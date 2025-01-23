package kz.amihady.category.service.mapper;


import kz.amihady.category.entity.Category;
import kz.amihady.category.service.request.CreateCategoryRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CategoryRequestMapper {
    public Category mapToCategory(Long userId, CreateCategoryRequest request){
        log.info("Создание категории с именем: {}", request.categoryName());
        Category category = new Category();
        category.setCategoryName(request.categoryName());
        category.setUserId(userId);
        return category;
    }
}
