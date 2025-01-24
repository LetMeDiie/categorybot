package kz.amihady.telegrambot.service.command;

import kz.amihady.telegrambot.factory.CommandFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CommandManager {
    private final Map<Long, Command> commandMap = new HashMap<>();

    @Autowired
    private CommandFactory commandFactory;

    public Command getCommandForUser(Message message) {
        Long userId = message.getChatId();
        log.info("Запрос команды для пользователя: {}", userId);

        Command command = commandMap.get(userId);

        if (command == null) {
            log.info("Пользователь {} не имеет активной команды. Создание новой...", userId);
            command = commandFactory.createCommand(message);
            commandMap.put(userId, command);
            log.info("Создана новая команда: {}", command.getClass().getSimpleName());
        } else {
            log.info("Возвращаем текущую команду для пользователя {}: {}", userId, command.getClass().getSimpleName());
        }

        return command;
    }

    public void removeCommandForUser(Long userId) {
        if (commandMap.containsKey(userId)) {
            log.info("Удаление команды для пользователя {}", userId);
            commandMap.remove(userId);
        } else {
            log.warn("Попытка удалить команду для пользователя {}, но команды не найдено", userId);
        }
    }
}