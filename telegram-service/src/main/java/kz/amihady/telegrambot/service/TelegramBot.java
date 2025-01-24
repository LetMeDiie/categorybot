package kz.amihady.telegrambot.service;

import kz.amihady.telegrambot.config.BotConfig;

import kz.amihady.telegrambot.service.command.Command;
import kz.amihady.telegrambot.service.command.CommandManager;
import kz.amihady.telegrambot.service.responsesender.TelegramResponseSender;
import kz.amihady.telegrambot.utils.CommandType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
@FieldDefaults(level= AccessLevel.PRIVATE,makeFinal = true)
@Slf4j
public class TelegramBot extends TelegramLongPollingBot {
    BotConfig botConfig;
    CommandManager commandManager;

    @Override
    public void onUpdateReceived(Update update){ 
        if(!update.hasMessage())return;
        Message message = update.getMessage();
        Command command = commandManager.getCommandForUser(message);
        TelegramResponseSender responseSender = command.process(message);
        if(command.isCompleted()) {
            log.info("Команда была завершена");
            commandManager.removeCommandForUser(message.getChatId());
        }
        responseSender.sendResponse(this,message.getChatId());
    }


    public void registerCommands() {
        List<BotCommand> commands = new ArrayList<>();
        for (CommandType command : CommandType.values()) {
            commands.add(new BotCommand(command.getCommandName(), command.getDescription()));
        }
        SetMyCommands setMyCommands = new SetMyCommands(commands, null, null);
        try {
            execute(setMyCommands);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }
}
