package kz.amihady.category.exception;

public class CategoriesNotFoundException extends RuntimeException{

    public CategoriesNotFoundException(String message) {
        super(message);
    }
}
