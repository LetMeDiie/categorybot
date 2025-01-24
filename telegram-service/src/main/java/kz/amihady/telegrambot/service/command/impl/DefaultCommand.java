package kz.amihady.telegrambot.service.command.impl;

import kz.amihady.telegrambot.service.command.Command;
import kz.amihady.telegrambot.service.responsesender.TelegramResponseSender;
import kz.amihady.telegrambot.service.responsesender.impl.TelegramTextResponseSender;
import org.telegram.telegrambots.meta.api.objects.Message;

public class DefaultCommand implements Command {
    private String text;

    public DefaultCommand(String text) {
        this.text = text;
    }

    @Override
    public TelegramResponseSender process(Message message) {
        return new TelegramTextResponseSender(text);
    }

    @Override
    public boolean isCompleted() {
        return true;
    }
}
