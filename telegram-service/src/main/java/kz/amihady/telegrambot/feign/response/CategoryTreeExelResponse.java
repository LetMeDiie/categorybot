package kz.amihady.telegrambot.feign.response;


//ответ от category tree при удачной обработке категорию в файл
public record CategoryTreeExelResponse(
        byte [] file
) {
}
