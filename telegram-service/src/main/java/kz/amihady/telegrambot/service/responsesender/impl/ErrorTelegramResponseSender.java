package kz.amihady.telegrambot.service.responsesender.impl;


import kz.amihady.telegrambot.factory.ResponseMessageFactory;
import kz.amihady.telegrambot.service.responsesender.TelegramResponseSender;
import lombok.Getter;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
public class ErrorTelegramResponseSender implements TelegramResponseSender {
     private String errorMessage;

    public ErrorTelegramResponseSender(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public void sendResponse(TelegramLongPollingBot bot, Long chatId) {
        SendMessage sendMessage = ResponseMessageFactory.createMessage(this,chatId); // создаем сообщение
        try{
            bot.execute(sendMessage);
        }
        catch (TelegramApiException exception){
            exception.printStackTrace();
        }
    }
}
