package kz.amihady.category.service;

import kz.amihady.category.dto.CategoryDto;
import kz.amihady.category.entity.Category;
import kz.amihady.category.exception.CategoriesNotFoundException;
import kz.amihady.category.repository.CategoryRepository;
import kz.amihady.category.service.impl.CategoryServiceImpl;
import kz.amihady.category.service.mapper.CategoryDtoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplGetTreeTest {

    @Mock
    private CategoryRepository repository;

    @Mock
    private CategoryDtoMapper categoryDtoMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private static final Long USER_ID = 1L;

    @Test
    void testGetCategoryTree() {
        // Given
        List<Category> categories = List.of(new Category(1L, 1L, "Category1", null, new ArrayList<>()));
        List<CategoryDto> expectedDto = List.of(new CategoryDto(1L, -1L, "Category1"));

        // When
        when(repository.findByUserId(USER_ID)).thenReturn(categories);
        when(categoryDtoMapper.categoryToCategoryDto(categories)).thenReturn(expectedDto);

        List<CategoryDto> result = categoryService.getCategoryTree(USER_ID);

        // Then
        assertEquals(expectedDto, result);
        verify(repository, times(1)).findByUserId(USER_ID);  // Ensure repository was called once
        verify(categoryDtoMapper, times(1)).categoryToCategoryDto(categories);  // Ensure mapper was called once
    }

    @Test
    void testGetCategoryTree_CategoriesNotFound() {
        when(repository.findByUserId(USER_ID)).thenReturn(Collections.emptyList());

        CategoriesNotFoundException exception = assertThrows(
                CategoriesNotFoundException.class,
                () -> categoryService.getCategoryTree(USER_ID)
        );

        assertEquals("У данного пользователя нет категорий.", exception.getMessage());
        verify(repository).findByUserId(USER_ID);
        verifyNoInteractions(categoryDtoMapper);
    }
}
