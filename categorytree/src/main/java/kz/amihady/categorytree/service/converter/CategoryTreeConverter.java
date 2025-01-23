package kz.amihady.categorytree.service.converter;


import kz.amihady.categorytree.dto.CategoryDto;

import java.util.List;

public interface CategoryTreeConverter<T> {
    //передается список root категорий с дочерними элементами.
    T convert(List<CategoryDto> categoryDtoList);
}
