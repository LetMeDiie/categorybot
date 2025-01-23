package kz.amihady.categorytree.dto;

public record CategoryDto(
        Long categoryId,
        Long parentId,
        String categoryName
) {
}
