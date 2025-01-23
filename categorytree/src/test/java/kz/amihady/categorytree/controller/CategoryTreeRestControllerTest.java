package kz.amihady.categorytree.controller;

import feign.FeignException;
import kz.amihady.categorytree.exception.CategoryTreeConversionFileException;
import kz.amihady.categorytree.service.CategoryTreeService;
import kz.amihady.categorytree.service.response.CategoryTreeStringResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@WebMvcTest(CategoryTreeRestController.class)
class CategoryTreeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryTreeService categoryTreeService;

    @Test
    void testViewTree_Success() throws Exception {
        // Подготовка данных
        Long userId = 1L;
        String mockTreeResponse = "• 📂 Категория 1\n   → 📄 Подкатегория 1.1\n";
        CategoryTreeStringResponse categoryTreeStringResponse = new CategoryTreeStringResponse(mockTreeResponse);

        // Мокаем поведение сервиса
        when(categoryTreeService.getTreeAsString(userId)).thenReturn(categoryTreeStringResponse);

        // Выполняем GET-запрос
        mockMvc.perform(get("/viewTree/{userId}", userId))
                .andExpect(status().isOk())  // Проверяем, что статус ответа OK
                .andExpect(jsonPath("$.categoryTree").value(mockTreeResponse));  // Проверяем, что ответ соответствует ожидаемому

        // Проверяем, что метод сервиса был вызван один раз с правильным аргументом
        verify(categoryTreeService, times(1)).getTreeAsString(userId);
    }



    @Test
    void testViewTree_Error() throws Exception {
        // Подготовка данных
        Long userId = 1L;

        // Мокаем, что сервис выбросит исключение
        when(categoryTreeService.getTreeAsString(userId)).thenThrow(new CategoryTreeConversionFileException("Ошибка при получении дерева"));

        // Выполняем GET-запрос и проверяем, что выбрасывается ошибка 400
        mockMvc.perform(get("/viewTree/{userId}", userId))
                .andExpect(status().isBadRequest())  // Статус ответа должен быть 400
                .andExpect(jsonPath("$.message").value("Ошибка при получении дерева"));  // Проверяем сообщение ошибки

        // Проверяем, что метод сервиса был вызван один раз с правильным аргументом
        verify(categoryTreeService, times(1)).getTreeAsString(userId);
    }

    @Test
    void testDownload_CategoryTreeConversionFileException() throws Exception {
        // Подготовка данных
        Long userId = 1L;

        // Мокаем, что сервис выбросит исключение
        when(categoryTreeService.getTreeAsExel(userId)).thenThrow(new CategoryTreeConversionFileException("Ошибка при скачивании Excel"));

        // Выполняем GET-запрос и проверяем, что выбрасывается ошибка 400
        mockMvc.perform(get("/download/{userId}", userId))
                .andExpect(status().isBadRequest())  // Статус ответа должен быть 400
                .andExpect(jsonPath("$.errorMessage").value("Ошибка при скачивании Excel"));  // Проверяем сообщение ошибки

        // Проверяем, что метод сервиса был вызван один раз с правильным аргументом
        verify(categoryTreeService, times(1)).getTreeAsExel(userId);
    }

    @Test
    void testViewTree_Fail_FeignException() throws Exception {
        Long userId = 1L;

        // Мокаем исключение FeignException
        when(categoryTreeService.getTreeAsString(userId))
                .thenThrow(FeignException.class); // Бросаем FeignException

        // Выполняем запрос
        mockMvc.perform(get("/viewTree/{userId}", userId))
                .andExpect(status().isInternalServerError()) // Ожидаем код 500
                .andExpect(jsonPath("$.errorMessage").value("Ошибка при обращении к удаленному сервису")); // Проверяем сообщение об ошибке
    }

    @Test
    void testDownload_Fail_FeignException() throws Exception {
        Long userId = 1L;

        when(categoryTreeService.getTreeAsExel(userId))
                .thenThrow(FeignException.class);

        // Выполняем запрос
        mockMvc.perform(get("/download/{userId}", userId))
                .andExpect(status().isInternalServerError()) // Ожидаем код 500
                .andExpect(jsonPath("$.errorMessage").value("Ошибка при обращении к удаленному сервису")); // Проверяем сообщение об ошибке
    }
}