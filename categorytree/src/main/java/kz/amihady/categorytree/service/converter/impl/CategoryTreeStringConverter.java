package kz.amihady.categorytree.service.converter.impl;




import kz.amihady.categorytree.dto.CategoryDto;
import kz.amihady.categorytree.service.converter.CategoryTreeConverter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// преобразуем категорию в строку
public class CategoryTreeStringConverter implements CategoryTreeConverter<String> {

    @Override
    public String convert(List<CategoryDto> categoryDtoList) {
        Map<Long, List<CategoryDto>> categoryMap = categoryDtoList.stream()
                .collect(Collectors.groupingBy(CategoryDto::parentId));

        StringBuilder result = new StringBuilder();
        buildTree(categoryMap, -1L, 0, result);

        return result.toString();
    }

    private void buildTree(Map<Long, List<CategoryDto>> categoryMap, Long parentId, int level, StringBuilder result) {
        List<CategoryDto> children = categoryMap.getOrDefault(parentId, Collections.emptyList());

        for (CategoryDto category : children) {
            String icon = (parentId == -1) ? "📂" : (categoryMap.containsKey(category.categoryId()) ? "📁" : "📄");
            String indent = " ".repeat(level); // Создаем отступы
            String bullet = (level == 0) ? "• " : "→ "; // Верхний уровень - "•", остальные - "→"

            result.append(indent)
                    .append(bullet)
                    .append(icon)
                    .append(" ")
                    .append(category.categoryName())
                    .append("\n");

            buildTree(categoryMap, category.categoryId(), level + 1, result);
        }
    }
}