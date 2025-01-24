package kz.amihady.telegrambot.feign;

import kz.amihady.telegrambot.feign.fallback.CategoryTreeServiceFallBack;
import kz.amihady.telegrambot.feign.response.CategoryTreeExelResponse;
import kz.amihady.telegrambot.feign.response.CategoryTreeStringResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "category-tree", url = "${service.url.categoryTree}",fallback = CategoryTreeServiceFallBack.class)
public interface CategoryTreeServiceClient {
    @GetMapping("/viewTree/{userId}")
    CategoryTreeStringResponse getTreeAsString(@PathVariable("userId") Long userId);

    @GetMapping("/download/{userId}")
    CategoryTreeExelResponse getTreeAsFile(@PathVariable("userId") Long userId);

}