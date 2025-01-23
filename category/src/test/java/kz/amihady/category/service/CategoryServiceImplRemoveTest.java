package kz.amihady.category.service;


import kz.amihady.category.entity.Category;
import kz.amihady.category.exception.CategoryNotFoundException;
import kz.amihady.category.repository.CategoryRepository;
import kz.amihady.category.service.impl.CategoryServiceImpl;
import kz.amihady.category.service.response.CategoryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplRemoveTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private final Long USER_ID = 1L;
    private final String CATEGORY_NAME = "TestCategory";

    @Test
    void testRemoveCategory_Success() {
        Category category = new Category();
        category.setUserId(USER_ID);
        category.setCategoryName(CATEGORY_NAME);

        when(repository.findByUserIdAndCategoryName(USER_ID, CATEGORY_NAME))
                .thenReturn(Optional.of(category));

        CategoryResponse response = categoryService.removeCategory(USER_ID, CATEGORY_NAME);

        assertEquals(String.format("Категория '%s' была успешно удалена.", CATEGORY_NAME), response.message());
    }

    @Test
    void testRemoveCategory_ThrowsCategoryNotFoundException() {
        when(repository.findByUserIdAndCategoryName(USER_ID, CATEGORY_NAME))
                .thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.removeCategory(USER_ID, CATEGORY_NAME);
        });
    }
}
