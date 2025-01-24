package kz.amihady.category.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.amihady.category.dto.CategoryDto;
import kz.amihady.category.exception.CategoriesNotFoundException;
import kz.amihady.category.exception.CategoryAlreadyExistsException;
import kz.amihady.category.exception.CategoryNotFoundException;
import kz.amihady.category.service.CategoryService;
import kz.amihady.category.service.request.CreateCategoryRequest;
import kz.amihady.category.service.response.CategoryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryRestController.class)
public class CategoryRestControllerTest {

    private static final String BASE_URL = "/api/{userId}";
    private static final Long USER_ID = 1L;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CategoryService categoryService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void testGetCategoryTree_Success() throws Exception {
        // Given
        List<CategoryDto> categoryDto = List.of(new CategoryDto(1L, -1L, "category"));

        // Mock the service method call
        when(categoryService.getCategoryTree(USER_ID)).thenReturn(categoryDto);

        // Perform the request and validate the response
        mockMvc.perform(
                        get(BASE_URL, USER_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())  // Expect OK status
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))  // Expect JSON response
                .andExpect(jsonPath("$[0].categoryId").value(1L))  // Assert that the first CategoryDto has categoryId 1L
                .andExpect(jsonPath("$[0].parentId").value(-1L))  // Assert that the parentId is -1 (no parent)
                .andExpect(jsonPath("$[0].categoryName").value("category"));  // Assert that categoryName is "category"
    }

    @Test
    public void testGetCategoryTree_NotFound() throws Exception {
        when(categoryService.getCategoryTree(USER_ID))
                .thenThrow(new CategoriesNotFoundException("Categories not found"));

        mockMvc.perform(
                        get(BASE_URL, USER_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage").value("Categories not found"));
    }

    @Test
    public void testDeleteCategory_Success() throws Exception {
        String categoryName = "TestCategory";
        CategoryResponse response = new CategoryResponse("Category deleted successfully");

        when(categoryService.removeCategory(USER_ID, categoryName))
                .thenReturn(response);

        mockMvc.perform(
                        delete(BASE_URL + "/" + categoryName, USER_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Category deleted successfully"));
    }

    @Test
    public void testDeleteCategory_NotFound() throws Exception {
        String categoryName = "TestCategory";

        when(categoryService.removeCategory(USER_ID, categoryName))
                .thenThrow(new CategoryNotFoundException("Category not found"));

        mockMvc.perform(
                        delete(BASE_URL + "/" + categoryName, USER_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage").value("Category not found"));
    }



    @Test
    public void testAddRootCategory_Success() throws Exception {
        CreateCategoryRequest request = new CreateCategoryRequest("New Category");
        CategoryResponse categoryResponse = new CategoryResponse("Added new category");

        when(categoryService.addRootCategory(USER_ID,request)).thenReturn(categoryResponse);

        mockMvc.perform(post(BASE_URL,USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryName\":\"New Category\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Added new category"));
    }

    @Test
    public void testAddRootCategory_CategoryAlreadyExists() throws Exception {
        CreateCategoryRequest request = new CreateCategoryRequest("Existing Category");

        when(categoryService.addRootCategory(USER_ID,request))
                .thenThrow(new CategoryAlreadyExistsException("Category with name " + request.categoryName() + " already exists"));

        mockMvc.perform(post(BASE_URL , USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"categoryName\":\"Existing Category\"}"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorMessage").value("Category with name Existing Category already exists"));

    }

    @Test
    public void testAddChildCategory_Success() throws Exception {
        String parentCategoryName = "ParentCategory";
        CreateCategoryRequest request = new CreateCategoryRequest("ChildCategory");
        CategoryResponse response = new CategoryResponse("Category added successfully");

        when(categoryService.addChildCategory(USER_ID, parentCategoryName, request))
                .thenReturn(response);

        mockMvc.perform(
                        post(BASE_URL + "/" + parentCategoryName, USER_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Category added successfully"));
    }
    @Test
    public void testAddChildCategory_ParentNotFound() throws Exception {
        String parentCategoryName = "NonExistentParent";
        CreateCategoryRequest request = new CreateCategoryRequest("ChildCategory");

        when(categoryService.addChildCategory(USER_ID, parentCategoryName, request))
                .thenThrow(new CategoryNotFoundException("Parent not found"));

        mockMvc.perform(
                        post(BASE_URL + "/" + parentCategoryName, USER_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage").value("Parent not found"));
    }

    @Test
    public void testAddChildCategory_AlreadyExists() throws Exception {
        String parentCategoryName = "ParentCategory";
        CreateCategoryRequest request = new CreateCategoryRequest("ExistingCategory");

        when(categoryService.addChildCategory(USER_ID, parentCategoryName, request))
                .thenThrow(new CategoryAlreadyExistsException("Category already exists"));

        mockMvc.perform(
                        post(BASE_URL + "/" + parentCategoryName, USER_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage").value("Category already exists"));
    }




}
