package kz.amihady.categorytree.factory;



import kz.amihady.categorytree.service.CategoryTreeFormat;
import kz.amihady.categorytree.service.converter.CategoryTreeConverter;
import kz.amihady.categorytree.service.converter.impl.CategoryTreeExelConverter;
import kz.amihady.categorytree.service.converter.impl.CategoryTreeStringConverter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CategoryTreeConverterFactory {
    private final Map<CategoryTreeFormat, CategoryTreeConverter<?>> converters = new HashMap<>();

    public CategoryTreeConverterFactory() {
        // Добавление конвертеров
        converters.put(CategoryTreeFormat.STRING, new CategoryTreeStringConverter());
        converters.put(CategoryTreeFormat.EXEL, new CategoryTreeExelConverter());
    }

    public CategoryTreeConverter<?> getConverter(CategoryTreeFormat format) {
        if(format ==null  || !converters.containsKey(format))
            throw new IllegalArgumentException("Ошибка работы в системе. Сообщите админу");
        return converters.get(format);
    }
}