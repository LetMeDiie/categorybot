package kz.amihady.categorytree.feign;

import kz.amihady.categorytree.dto.CategoryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(name = "category", url = "${category.service.url}")
//получаем список категорий из категория сервис.
//categoryDto = categoryId,parentId(если parent отсутствует == -1),categoryName
public interface CategoryServiceClient {

    @GetMapping("/{userId}")
    List<CategoryDto> getCategoryDto(@PathVariable("userId") Long userId);

}
