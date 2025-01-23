package kz.amihady.categorytree.service;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import feign.FeignException;
import kz.amihady.categorytree.exception.CategoryTreeConversionFileException;
import kz.amihady.categorytree.service.converter.ResponseTypeConverter;
import kz.amihady.categorytree.service.impl.CategoryTreeServiceImpl;
import kz.amihady.categorytree.service.response.CategoryTreeExelResponse;
import kz.amihady.categorytree.service.response.CategoryTreeStringResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class CategoryTreeServiceImplTest {

    @Mock
    private ResponseTypeConverter responseTypeConverter;

    private CategoryTreeServiceImpl categoryTreeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoryTreeService = new CategoryTreeServiceImpl(responseTypeConverter);
    }

    @Test
    void testGetTreeAsString_Success() {
        Long userId = 1L;
        String expectedCategoryTree = "• 📂 Категория 1\n   → 📁 Подкатегория 1.1\n";

        // Мокаем поведение метода конвертера
        when(responseTypeConverter.convert(CategoryTreeFormat.STRING, userId))
                .thenReturn(expectedCategoryTree);

        CategoryTreeStringResponse response = categoryTreeService.getTreeAsString(userId);

        assertNotNull(response);
        assertEquals(expectedCategoryTree, response.categoryTree());
    }


    @Test
    void testGetTreeAsString_Exception() {
        Long userId = 1L;

        when(responseTypeConverter.convert(CategoryTreeFormat.STRING, userId))
                .thenThrow(FeignException.class);

        assertThrows(FeignException.class, () -> categoryTreeService.getTreeAsString(userId));
    }


    @Test
    void testGetTreeAsExel_Success() {
        // Подготовка данных
        Long userId = 1L;
        byte[] expectedFile = new byte[]{1, 2, 3, 4}; // Пример байтового массива

        // Мокаем поведение метода конвертера
        when(responseTypeConverter.convert(CategoryTreeFormat.EXEL, userId))
                .thenReturn(expectedFile);

        CategoryTreeExelResponse response = categoryTreeService.getTreeAsExel(userId);

        assertNotNull(response);
        assertArrayEquals(expectedFile, response.file());
    }


    @Test
    void testGetTreeAsExel_FeignException() {
        Long userId = 1L;

        when(responseTypeConverter.convert(CategoryTreeFormat.EXEL, userId))
                .thenThrow(FeignException.class);

        FeignException exception = assertThrows(FeignException.class, () -> categoryTreeService.getTreeAsExel(userId));

        assertThrows(FeignException.class, () -> categoryTreeService.getTreeAsExel(userId));
    }

    @Test
    void testGetTreeAsExel_CategoryTreeConversionFileException() {
        Long userId = 1L;

        when(responseTypeConverter.convert(CategoryTreeFormat.EXEL, userId))
                .thenThrow(new CategoryTreeConversionFileException("Conversion to Excel failed"));

        CategoryTreeConversionFileException exception = assertThrows(CategoryTreeConversionFileException.class, () -> categoryTreeService.getTreeAsExel(userId));

        assertEquals("Conversion to Excel failed", exception.getMessage());
    }
}
