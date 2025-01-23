package kz.amihady.categorytree.service.converter;


import kz.amihady.categorytree.feign.CategoryServiceClient;
import kz.amihady.categorytree.dto.CategoryDto;
import kz.amihady.categorytree.factory.CategoryTreeConverterFactory;
import kz.amihady.categorytree.service.CategoryTreeFormat;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

// что то вроде не знаю даже , занимается преобразованием полученных данных от конвертов в нужный тип.
@Component
@RequiredArgsConstructor
public class ResponseTypeConverter {
    private final CategoryTreeConverterFactory categoryTreeConverterFactory;
    private final CategoryServiceClient categoryServiceClient;


    public <T> T convert(CategoryTreeFormat format,Long userId) {
        List<CategoryDto> categoryDtoList =
                categoryServiceClient.getCategoryDto(userId); // получаем весь список категорий
        CategoryTreeConverter<?> converter = categoryTreeConverterFactory.getConverter(format);
        return (T) converter.convert(categoryDtoList);

    }
}