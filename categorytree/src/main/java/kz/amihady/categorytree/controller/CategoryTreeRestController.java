package kz.amihady.categorytree.controller;

import kz.amihady.categorytree.service.CategoryTreeService;
import kz.amihady.categorytree.service.response.CategoryTreeExelResponse;
import kz.amihady.categorytree.service.response.CategoryTreeStringResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CategoryTreeRestController {

    CategoryTreeService categoryTreeService;

    @GetMapping("/viewTree/{userId}")
    public ResponseEntity<CategoryTreeStringResponse> viewTree(@PathVariable("userId") Long userId) {
        log.info("Получен запрос на отображение дерева категорий для пользователя с ID: {}", userId);

        CategoryTreeStringResponse response = categoryTreeService.getTreeAsString(userId);
        log.info("Дерево категорий успешно получено для пользователя с ID: {}", userId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/download/{userId}")
    public ResponseEntity<CategoryTreeExelResponse> download(@PathVariable("userId") Long userId) {
        log.info("Получен запрос на загрузку дерева категорий в формате Excel для пользователя с ID: {}", userId);

        CategoryTreeExelResponse response = categoryTreeService.getTreeAsExel(userId);
        log.info("Файл Excel успешно сгенерирован для пользователя с ID: {}", userId);

        return ResponseEntity.ok(response);
    }
}

