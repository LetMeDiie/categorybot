package kz.amihady.telegrambot.service.responsesender.impl;


import kz.amihady.telegrambot.factory.ResponseMessageFactory;
import kz.amihady.telegrambot.service.responsesender.TelegramResponseSender;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
@Setter
public class TelegramTextResponseSender implements TelegramResponseSender {

    private String message;


    public TelegramTextResponseSender(String message) {
        this.message = message;
    }

    @Override
    public void sendResponse(TelegramLongPollingBot bot,Long chatId) {
        SendMessage sendMessage = ResponseMessageFactory.createMessage(this,chatId);
        try{
            bot.execute(sendMessage);
        }
        catch (TelegramApiException exception){
            exception.printStackTrace();
        }
    }
}
