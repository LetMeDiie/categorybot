package kz.amihady.category.controller;



import kz.amihady.category.dto.CategoryDto;
import kz.amihady.category.service.CategoryService;
import kz.amihady.category.service.request.CreateCategoryRequest;
import kz.amihady.category.service.response.CategoryResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
@RequestMapping(value ="/api/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)

public class CategoryRestController {

    CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<List<CategoryDto>> getCategoryTree(
            @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(categoryService.getCategoryTree(userId));
    }


    @DeleteMapping("/{categoryName}")
    public ResponseEntity<CategoryResponse> deleteCategory(
            @PathVariable("userId") Long userId,
            @PathVariable("categoryName") String categoryName){
        return ResponseEntity.ok(categoryService.removeCategory(userId,categoryName));
    }

    @PostMapping("/{parentCategoryName}")
    public ResponseEntity<CategoryResponse> addChildCategory(
            @PathVariable("userId") Long userId,
            @PathVariable("parentCategoryName") String parentCategoryName,
            @RequestBody CreateCategoryRequest request){
        CategoryResponse response =
                categoryService.addChildCategory(userId,parentCategoryName,request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping()
    public ResponseEntity<CategoryResponse> addRootCategory(
            @PathVariable("userId") Long userId,
            @RequestBody CreateCategoryRequest request) {
        CategoryResponse categoryResponse =
                categoryService.addRootCategory(userId,request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryResponse);
    }



}
