package kz.amihady.telegrambot.factory;


import kz.amihady.telegrambot.service.responsesender.impl.ErrorTelegramResponseSender;
import kz.amihady.telegrambot.service.responsesender.impl.TelegramFileResponseSender;
import kz.amihady.telegrambot.service.responsesender.impl.TelegramTextResponseSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.ByteArrayInputStream;
//методы этого класса статические , возможно нарушен немного принцип срп и для таких случаем не рекомендуется создание статических методо
//внедрять этот класс очень было сложно и в классах не хотел явно их создавать
//да и методы создание сообщение вряд ли изменятся.
public class ResponseMessageFactory {

    /**
     * Создает объект SendMessage для текстового ответа.
     *
     * @param response Ответ типа TelegramTextResponseSender
     * @return Объект SendMessage
     */
    public static SendMessage createMessage(TelegramTextResponseSender response , Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(response.getMessage()); // Текст сообщения
        sendMessage.setChatId(String.valueOf(chatId));
        return sendMessage;
    }

    /**
     * Создает объект SendDocument для файлового ответа.
     *
     * @param response Ответ типа TelegramFileResponseSender
     * @return Объект SendDocument
     */
    public static SendDocument createMessage(TelegramFileResponseSender response , Long chatId) {
        // Преобразуем массив байтов в InputStream
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(response.getFileData());

        // Создаем InputFile с InputStream и названием файла
        InputFile inputFile = new InputFile(byteArrayInputStream, response.getFileName());

        // Создаем SendDocument для отправки файла
        SendDocument sendDocument = new SendDocument();
        sendDocument.setDocument(inputFile);
        sendDocument.setChatId(String.valueOf(chatId));


        return sendDocument;
    }

    /**
     * Создает объект SendMessage для текстового ответа об ошибке.
     *
     * @param errorResponse Ответ типа ErrorTelegramResponseSender
     * @return Объект SendMessage
     */
    public static SendMessage createMessage(ErrorTelegramResponseSender errorResponse , Long chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(errorResponse.getErrorMessage());
        sendMessage.setChatId(String.valueOf(chatId));
        return sendMessage;
    }
}