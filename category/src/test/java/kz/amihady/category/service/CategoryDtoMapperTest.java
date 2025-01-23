package kz.amihady.category.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import kz.amihady.category.dto.CategoryDto;
import kz.amihady.category.entity.Category;
import kz.amihady.category.service.mapper.CategoryDtoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

class CategoryDtoMapperTest {

    private CategoryDtoMapper categoryDtoMapper;

    @BeforeEach
    void setUp() {
        categoryDtoMapper = new CategoryDtoMapper();
    }

    @Test
    void testCategoryToCategoryDto() {
        // Given
        Category parentCategory = mock(Category.class);
        when(parentCategory.getId()).thenReturn(1L);

        Category category = mock(Category.class);
        when(category.getId()).thenReturn(2L);
        when(category.getParent()).thenReturn(parentCategory);
        when(category.getCategoryName()).thenReturn("Electronics");

        List<Category> categoryList = Arrays.asList(category);

        // When
        List<CategoryDto> categoryDtoList = categoryDtoMapper.categoryToCategoryDto(categoryList);

        // Then
        assertEquals(1, categoryDtoList.size());
        CategoryDto categoryDto = categoryDtoList.get(0);
        assertEquals(2L, categoryDto.categoryId());
        assertEquals(1L, categoryDto.parentId());
        assertEquals("Electronics", categoryDto.categoryName());
    }

    @Test
    void testCategoryToCategoryDto_WithNoParent() {
        // Given
        Category category = mock(Category.class);
        when(category.getId()).thenReturn(2L);
        when(category.getParent()).thenReturn(null);
        when(category.getCategoryName()).thenReturn("Toys");

        List<Category> categoryList = Arrays.asList(category);

        // When
        List<CategoryDto> categoryDtoList = categoryDtoMapper.categoryToCategoryDto(categoryList);

        // Then
        assertEquals(1, categoryDtoList.size());
        CategoryDto categoryDto = categoryDtoList.get(0);
        assertEquals(2L, categoryDto.categoryId());
        assertEquals(-1L, categoryDto.parentId()); // Parent should be -1
        assertEquals("Toys", categoryDto.categoryName());
    }
}
