package kz.amihady.telegrambot.service.command.impl;

import kz.amihady.telegrambot.service.command.Command;
import kz.amihady.telegrambot.service.responsesender.TelegramResponseSender;
import kz.amihady.telegrambot.service.responsesender.impl.TelegramTextResponseSender;
import kz.amihady.telegrambot.utils.CommandType;
import org.telegram.telegrambots.meta.api.objects.Message;

//одна шаговая команда получаем запрос на команду и сразу же возвращаем ответ
public class HelpCommand implements Command {
    @Override
    public TelegramResponseSender process(Message message) {
        String helpText = "Доступные команды:";
        for(CommandType commandType:CommandType.values()){
            helpText+="\n"+commandType.getCommandName()+":"+commandType.getDescription();
        }
        return new TelegramTextResponseSender(helpText);
    }

    @Override
    public boolean isCompleted() {
        return true;
    }
}
