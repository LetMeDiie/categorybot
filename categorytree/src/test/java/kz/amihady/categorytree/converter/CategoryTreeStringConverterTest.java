package kz.amihady.categorytree.converter;
import kz.amihady.categorytree.dto.CategoryDto;
import kz.amihady.categorytree.service.converter.impl.CategoryTreeStringConverter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;

public class CategoryTreeStringConverterTest {

    @Test
    public void testConvertSingleLevel() {
        // Создаем тестовые данные для одной категории
        List<CategoryDto> categoryDtoList = List.of(
                new CategoryDto(1L, -1L, "Категория 1")
        );

        // Инициализируем конвертер
        CategoryTreeStringConverter converter = new CategoryTreeStringConverter();

        // Ожидаемый результат
        String expected = "• 📂 Категория 1\n";

        // Получаем результат
        String result = converter.convert(categoryDtoList);

        // Проверяем, что результат соответствует ожиданиям
        assertEquals(expected, result);
    }


    @Test
    public void testConvertNoParentCategories() {
        // Создаем тестовые данные для категорий без родительских элементов
        List<CategoryDto> categoryDtoList = List.of(
                new CategoryDto(1L, -1L, "Категория 1"),
                new CategoryDto(2L, -1L, "Категория 2")
        );

        // Инициализируем конвертер
        CategoryTreeStringConverter converter = new CategoryTreeStringConverter();

        // Ожидаемый результат
        String expected = "• 📂 Категория 1\n" +
                          "• 📂 Категория 2\n";

        // Получаем результат
        String result = converter.convert(categoryDtoList);

        // Проверяем, что результат соответствует ожиданиям
        assertEquals(expected, result);
    }

    @Test
    public void testCategoryTreeStringConversion() {
        // Список категорий для теста
        List<CategoryDto> categories = List.of(
                new CategoryDto(1L, -1L, "Категория 1"),
                new CategoryDto(2L, 1L, "Подкатегория 1.1")
        );

        // Строка, которую мы ожидаем
        String expectedOutput =
                        "• 📂 Категория 1\n" +
                        " → 📄 Подкатегория 1.1\n";

        // Конвертируем с помощью CategoryTreeStringConverter
        CategoryTreeStringConverter converter = new CategoryTreeStringConverter();
        String result = converter.convert(categories);

        // Сравниваем результат с ожидаемым
        assertEquals(expectedOutput, result);
    }
}
