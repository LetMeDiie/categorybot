package kz.amihady.categorytree.service.impl;


import kz.amihady.categorytree.service.CategoryTreeFormat;
import kz.amihady.categorytree.service.CategoryTreeService;
import kz.amihady.categorytree.service.converter.ResponseTypeConverter;
import kz.amihady.categorytree.service.response.CategoryTreeExelResponse;
import kz.amihady.categorytree.service.response.CategoryTreeStringResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
public class CategoryTreeServiceImpl implements CategoryTreeService {
    ResponseTypeConverter responseTypeConverter; // метод конверт может вызввать исключение в случае ошибок при генераций файлов и т д

    @Override
    public CategoryTreeStringResponse getTreeAsString(Long userId) {
        String categoryTree =
                responseTypeConverter.convert(CategoryTreeFormat.STRING,userId);
        return new CategoryTreeStringResponse(categoryTree);

    }

    @Override
    public CategoryTreeExelResponse getTreeAsExel(Long userId) {
        byte[] file = responseTypeConverter
                .convert(CategoryTreeFormat.EXEL,userId);
        return new CategoryTreeExelResponse(file);
    }
}

