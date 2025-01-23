package kz.amihady.category.dto;

public record CategoryDto(
        Long categoryId,
        Long parentId,
        String categoryName
) {
}
