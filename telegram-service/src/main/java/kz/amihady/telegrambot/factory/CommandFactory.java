package kz.amihady.telegrambot.factory;

import kz.amihady.telegrambot.feign.CategoryTreeServiceClient;
import kz.amihady.telegrambot.service.command.Command;
import kz.amihady.telegrambot.service.command.impl.*;
import kz.amihady.telegrambot.utils.CommandType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class CommandFactory {
    @Autowired
    CommandStepFactory commandStepFactory;

    @Autowired
    CategoryTreeServiceClient categoryTreeService;
    public Command createCommand(Message message) {
        if (!message.hasText()) {
            return new DefaultCommand("Что бы выбрать команду введите в виде строки.");
        }
        String input = message.getText();
        CommandType commandType = CommandType.getCommand(input);

        if (commandType == null) {
            return new DefaultCommand("Не удалось опредедить команду.\nВведите /help что бы получить инструкцию. ");
        }
        return switch (commandType) {
            case DOWNLOAD -> new DownloadCommand(categoryTreeService);
            case VIEWTREE -> new ViewTreeCommand(categoryTreeService);
            case HELP -> new HelpCommand();
            case ROOT -> new DefaultCommand("Это вспомогательная команда для команды /addelement.Нельзя ее тут использовать.");
            case REMOVEELEMENT,ADDELEMENT -> stepCommandFactor(commandType);
            case CANCEL -> new DefaultCommand("Это команда что бы отменять другие команды.\nНевозможно его использовать пока вы не выберете команду.");
            default -> new DefaultCommand("Ошибка в коде у системы. Надо проверить.");
        };
    }
    private StepCommand stepCommandFactor(CommandType commandType){
        return new StepCommand(commandStepFactory
                .createStepsForCommand(commandType)
        );
    }
}
