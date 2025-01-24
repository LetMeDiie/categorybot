package kz.amihady.telegrambot.service.responsesender.impl;


import kz.amihady.telegrambot.factory.ResponseMessageFactory;
import kz.amihady.telegrambot.service.responsesender.TelegramResponseSender;
import lombok.Getter;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Getter
public class TelegramFileResponseSender implements TelegramResponseSender {
    private byte [] fileData;
    private String fileName;

    public TelegramFileResponseSender(byte[] fileData, String fileName) {
        this.fileData = fileData;
        this.fileName = fileName;
    }

    @Override
    public void sendResponse(TelegramLongPollingBot bot, Long chatId) {
        SendDocument document = ResponseMessageFactory.createMessage(this,chatId);
        try{
            bot.execute(document);
        }
        catch (TelegramApiException exception){
            exception.printStackTrace();
        }
    }

}
