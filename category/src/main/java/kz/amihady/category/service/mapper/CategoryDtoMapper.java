package kz.amihady.category.service.mapper;

import kz.amihady.category.dto.CategoryDto;
import kz.amihady.category.entity.Category;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CategoryDtoMapper {
    //превращаем каждую категорию в categoryDto
    public List<CategoryDto> categoryToCategoryDto(List<Category> categoryList) {
        List<CategoryDto> categoryDtoList =
                new ArrayList<>();
        for(Category category : categoryList) {
            categoryDtoList.add(categoryToCategoryDto(category));
        }
        return categoryDtoList;
    }
    private CategoryDto categoryToCategoryDto(Category category){
        Category parent = category.getParent();
        CategoryDto categoryDto = new CategoryDto(
                category.getId(),
                parent==null?-1:parent.getId(),
                category.getCategoryName()
        );
        return categoryDto;
    }
}
     