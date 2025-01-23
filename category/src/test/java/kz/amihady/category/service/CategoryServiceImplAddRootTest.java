package kz.amihady.category.service;


import kz.amihady.category.entity.Category;
import kz.amihady.category.exception.CategoryAlreadyExistsException;
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

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplAddRootTest {

    @Mock
    private CategoryRepository repository;

    @Mock
    private CategoryRequestMapper categoryRequestMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private static final Long USER_ID = 1L;

    @Test
    void testAddRootCategory_Success() {
        CreateCategoryRequest request = new CreateCategoryRequest("NewCategory");
        Category category = new Category(1L,1L,"NewCategory",null,new ArrayList<>());

        when(repository.findByUserIdAndCategoryName(USER_ID, "NewCategory")).thenReturn(Optional.empty());
        when(categoryRequestMapper.mapToCategory(USER_ID, request)).thenReturn(category);

        CategoryResponse response = categoryService.addRootCategory(USER_ID, request);

        assertNotNull(response);
        assertEquals("Категория с именем NewCategory была успешно добавлена.", response.message());
        verify(repository).findByUserIdAndCategoryName(USER_ID, "NewCategory");
        verify(repository).save(category);
    }

    @Test
    void testAddRootCategory_CategoryAlreadyExists() {
        CreateCategoryRequest request = new CreateCategoryRequest("ExistingCategory");
        Category existingCategory = new Category(1L,1L,"ExistingCategory",null,new ArrayList<>());

        when(repository.findByUserIdAndCategoryName(USER_ID, "ExistingCategory"))
                .thenReturn(Optional.of(existingCategory));

        CategoryAlreadyExistsException exception = assertThrows(
                CategoryAlreadyExistsException.class,
                () -> categoryService.addRootCategory(USER_ID, request)
        );

        assertEquals("Категория с именем 'ExistingCategory' уже существует", exception.getMessage());
        verify(repository).findByUserIdAndCategoryName(USER_ID, "ExistingCategory");
        verifyNoMoreInteractions(repository);
    }
}
