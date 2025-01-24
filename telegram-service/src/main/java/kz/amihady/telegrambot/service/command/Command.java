package kz.amihady.telegrambot.service.command;



import kz.amihady.telegrambot.service.responsesender.TelegramResponseSender;
import org.telegram.telegrambots.meta.api.objects.Message;



//в команду передается сам объект месседж полученный от телеграм
// это нужно что бы поулчить сообщение , проверить его тип , получить айди чата(пользователя)
//каждая команда будет возвращать респонссендер , который занимается отправкой ответа в телеграмм.
public interface Command {
    public TelegramResponseSender process(Message message);
    public boolean isCompleted();
}
