package kz.amihady.category.service.impl;


import kz.amihady.category.dto.CategoryDto;
import kz.amihady.category.entity.Category;
import kz.amihady.category.exception.CategoriesNotFoundException;
import kz.amihady.category.exception.CategoryAlreadyExistsException;
import kz.amihady.category.exception.CategoryNotFoundException;
import kz.amihady.category.repository.CategoryRepository;
import kz.amihady.category.service.CategoryService;
import kz.amihady.category.service.mapper.CategoryDtoMapper;
import kz.amihady.category.service.mapper.CategoryRequestMapper;
import kz.amihady.category.service.request.CreateCategoryRequest;
import kz.amihady.category.service.response.CategoryResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository repository;
    CategoryRequestMapper categoryRequestMapper;

    CategoryDtoMapper categoryDtoMapper;
    @Override
    public List<CategoryDto> getCategoryTree(Long userId) {
        log.info("Запрос списка категорий для пользователя с ID: {}", userId);
        List<Category> categoryList =
                repository.findByUserId(userId); //получаем список всех категории
        if(categoryList.isEmpty()) {
            log.warn("У пользователя с ID {} нет категорий.", userId);
            throw  new CategoriesNotFoundException("У данного пользователя нет категорий.");
        }
        log.info("Найдено {} категорий для пользователя с ID: {}", categoryList.size(), userId);
        return categoryDtoMapper.categoryToCategoryDto(categoryList);
    }

    @Override
    public CategoryResponse addRootCategory(Long userId, CreateCategoryRequest request) {
        log.info("Запрос на добавление корневой категории для пользователя с ID: {}", userId);
        //проверка на уникальность имени
        checkIfCategoryExists(userId, request.categoryName());

        //превращаем запрос на сущность
        Category rootCategory=categoryRequestMapper.mapToCategory(userId,request);

        log.info("Сохранение категории с именем: {}", request.categoryName());
        repository.save(rootCategory); // сохраняем

        String responseMessage = String.format("Категория с именем %s была успешно добавлена.", request.categoryName());
        log.info("Ответ: {}", responseMessage);

        return new CategoryResponse(responseMessage);
    }

    @Override
    public CategoryResponse addChildCategory(
                            Long userId,
                            String parentCategoryName ,
                            CreateCategoryRequest request) {
        log.info("Запрос на добавление дочерней категории '{}' для пользователя с ID: {}", request.categoryName(), userId);

        // получаем родительскую категорию
        Category parentCategory = getCategory(userId,parentCategoryName);
        //проверяем имя дочерней категорий на уникальность
        checkIfCategoryExists(userId, request.categoryName());

        log.info("Создание дочерней категории с именем '{}'", request.categoryName());
        Category childCategory = categoryRequestMapper.mapToCategory(userId,request);

        log.info("Добавление дочерней категории '{}' к родительской категории '{}'", request.categoryName(), parentCategoryName);
        parentCategory.addChild(childCategory);

        repository.save(parentCategory);

        // Возвращаем ответ
        String responseMessage = String.format("Дочерняя категория '%s' была добавлена к родительской категории '%s'.",
                request.categoryName(), parentCategoryName);
        log.info("Ответ: {}", responseMessage);

        return new CategoryResponse(responseMessage);
    }

    @Override
    public CategoryResponse removeCategory(Long userId,String categoryName) {
        log.info("Запрос на удаление категории '{}' для пользователя с ID: {}", categoryName, userId);
        //получаем категорию которую надо удалить
        Category category = getCategory(userId,categoryName);

        log.info("Удаление категории '{}' из базы данных", category.getCategoryName());
        repository.delete(category);

        // Возвращаем ответ
        String responseMessage = String.format("Категория '%s' была успешно удалена.", category.getCategoryName());
        log.info("Ответ: {}", responseMessage);
        return new CategoryResponse(responseMessage);
    }

    //попытка получить существующую категорию
    private Category getCategory(Long userId, String categoryName) {
        log.info("Попытка найти  категорию с именем '{}' для пользователя с ID: {}", categoryName, userId);
        return repository.findByUserIdAndCategoryName(userId, categoryName)
                .orElseThrow(() -> new CategoryNotFoundException(
                        "Категория '" + categoryName + "' не была найдена в система.\nУкажите существующее имя."));
    }

    //проверка имени категорий на уникальность данного пользователя
    private void checkIfCategoryExists(Long userId, String categoryName) {
        log.info("Проверка уникальности имени категории: {}", categoryName);
        repository.findByUserIdAndCategoryName(userId, categoryName)
                .ifPresent(c -> {
                    throw new CategoryAlreadyExistsException(
                            String.format("Категория с именем '%s' уже существует", categoryName)
                    );
                });
    }
}