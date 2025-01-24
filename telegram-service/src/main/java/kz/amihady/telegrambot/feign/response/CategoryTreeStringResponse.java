package kz.amihady.telegrambot.feign.response;


//ответ от сервиса category tree при удачной обработке категорию в строку
public record CategoryTreeStringResponse(
        String categoryTree
) {
}
