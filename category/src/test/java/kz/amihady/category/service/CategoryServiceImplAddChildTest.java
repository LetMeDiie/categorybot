package kz.amihady.category.service;


import kz.amihady.category.entity.Category;
import kz.amihady.category.exception.CategoryAlreadyExistsException;
import kz.amihady.category.exception.CategoryNotFoundException;
import kz.amihady.category.repository.CategoryRepository;
import kz.amihady.category.service.impl.CategoryServiceImpl;
import kz.amihady.category.service.mapper.CategoryRequestMapper;
import kz.amihady.category.service.request.CreateCategoryRequest;
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
class CategoryServiceImplAddChildTest {

    @Mock
    private CategoryRepository repository;

    @Mock
    private CategoryRequestMapper categoryRequestMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private final Long USER_ID = 1L;
    private final String PARENT_CATEGORY_NAME = "ParentCategory";
    private final String CHILD_CATEGORY_NAME = "ChildCategory";

    @Test
    void testAddChildCategory_Success() {
        Category parentCategory = new Category();
        parentCategory.setUserId(USER_ID);
        parentCategory.setCategoryName(PARENT_CATEGORY_NAME);

        CreateCategoryRequest request = new CreateCategoryRequest(CHILD_CATEGORY_NAME);
        Category childCategory = new Category();
        childCategory.setUserId(USER_ID);
        childCategory.setCategoryName(CHILD_CATEGORY_NAME);

        when(repository.findByUserIdAndCategoryName(USER_ID, PARENT_CATEGORY_NAME))
                .thenReturn(Optional.of(parentCategory));
        when(repository.findByUserIdAndCategoryName(USER_ID, CHILD_CATEGORY_NAME))
                .thenReturn(Optional.empty());
        when(categoryRequestMapper.mapToCategory(USER_ID, request)).thenReturn(childCategory);

        CategoryResponse response = categoryService.addChildCategory(USER_ID, PARENT_CATEGORY_NAME, request);

        assertEquals(
                String.format("Дочерняя категория '%s' была добавлена к родительской категории '%s'.",
                        CHILD_CATEGORY_NAME, PARENT_CATEGORY_NAME),
                response.message()
        );

        assertEquals(parentCategory, childCategory.getParent());
        assertTrue(parentCategory.getChildren().contains(childCategory));
    }

    @Test
    void testAddChildCategory_ThrowsCategoryNotFoundException() {
        CreateCategoryRequest request = new CreateCategoryRequest(CHILD_CATEGORY_NAME);

        when(repository.findByUserIdAndCategoryName(USER_ID, PARENT_CATEGORY_NAME))
                .thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> {
            categoryService.addChildCategory(USER_ID, PARENT_CATEGORY_NAME, request);
        });
    }

    @Test
    void testAddChildCategory_ThrowsCategoryAlreadyExistsException() {
        Category parentCategory = new Category();
        parentCategory.setUserId(USER_ID);
        parentCategory.setCategoryName(PARENT_CATEGORY_NAME);

        CreateCategoryRequest request = new CreateCategoryRequest(CHILD_CATEGORY_NAME);

        when(repository.findByUserIdAndCategoryName(USER_ID, PARENT_CATEGORY_NAME))
                .thenReturn(Optional.of(parentCategory));
        when(repository.findByUserIdAndCategoryName(USER_ID, CHILD_CATEGORY_NAME))
                .thenReturn(Optional.of(new Category()));

        assertThrows(CategoryAlreadyExistsException.class, () -> {
            categoryService.addChildCategory(USER_ID, PARENT_CATEGORY_NAME, request);
        });
    }
}
