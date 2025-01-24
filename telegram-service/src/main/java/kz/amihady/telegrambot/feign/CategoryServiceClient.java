package kz.amihady.telegrambot.feign;

import kz.amihady.telegrambot.feign.fallback.CategoryServiceFallback;
import kz.amihady.telegrambot.feign.request.CreateNewCategoryRequest;
import kz.amihady.telegrambot.feign.response.CategoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


//в случае если сервис категория недоступен будут вызываться методы категория сервис фалбак
@FeignClient(name = "category", url = "${service.url.category}",fallback = CategoryServiceFallback.class)
public interface CategoryServiceClient {

    @PostMapping("/{userId}")
    CategoryResponse addRootCategory(
            @PathVariable("userId")Long userId,
            @RequestBody CreateNewCategoryRequest request);


    @PostMapping("/{userId}/{parentCategoryName}")
     CategoryResponse addChildCategory
            (@PathVariable("userId") Long userId,
             @PathVariable("parentCategoryName") String parentCategoryName,
             @RequestBody CreateNewCategoryRequest request);


    @DeleteMapping("/{userId}/{categoryName}")
     CategoryResponse deleteCategory(
             @PathVariable("userId") Long userId,
             @PathVariable("categoryName") String categoryName);

}

