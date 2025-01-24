package kz.amihady.telegrambot.factory;

import kz.amihady.telegrambot.exception.ValidationException;
import kz.amihady.telegrambot.feign.request.CreateNewCategoryRequest;
import org.springframework.stereotype.Component;


@Component
public class CategoryRequestFactory {

    public CreateNewCategoryRequest createNewCategoryRequest(String categoryName) {
        // Проверка, что categoryName не null
        if (categoryName == null) {
            throw new ValidationException("Имя категории не может быть null.");
        }

        // Проверка длины имени категории (больше 2 и меньше 10 символов)
        if (categoryName.length() <= 2 || categoryName.length() >= 10) {
            throw new ValidationException("Имя категории должно быть длиной от 3 до 9 символов.");
        }

        if(categoryName.contains(" ")){
            throw  new ValidationException("Имя категорий не должен содержать пробелы");
        }

        if(categoryName.contains("/")){
            throw new ValidationException("Имя категорий не должен содержать /");
        }

        // Проверка, что имя категории начинается с буквы
        if (!Character.isLetter(categoryName.charAt(0))) {
            throw new ValidationException("Имя категории должно начинаться с буквы.");
        }


        // Если все проверки прошли успешно, возвращаем новый объект запроса
        return new CreateNewCategoryRequest(categoryName);
    }
}


