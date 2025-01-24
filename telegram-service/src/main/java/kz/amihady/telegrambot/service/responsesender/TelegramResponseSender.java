package kz.amihady.telegrambot.service.responsesender;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public interface TelegramResponseSender {
    void sendResponse(TelegramLongPollingBot bot , Long chatId);
}
