package kz.amihady.telegrambot.command;


import kz.amihady.telegrambot.service.command.impl.HelpCommand;
import kz.amihady.telegrambot.service.responsesender.TelegramResponseSender;
import kz.amihady.telegrambot.service.responsesender.impl.TelegramTextResponseSender;
import kz.amihady.telegrambot.utils.CommandType;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class HelpCommandTest {

    @Test
    void testHelpCommandResponse() {
        HelpCommand helpCommand = new HelpCommand();
        Message mockMessage = mock(Message.class);

        TelegramResponseSender responseSender = helpCommand.process(mockMessage);
        assertNotNull(responseSender);
        assertTrue(responseSender instanceof TelegramTextResponseSender);

        String responseText = ((TelegramTextResponseSender) responseSender).getMessage();
        assertNotNull(responseText);
        assertTrue(responseText.startsWith("Доступные команды:"));

        for (CommandType commandType : CommandType.values()) {
            assertTrue(responseText.contains(commandType.getCommandName()));
            assertTrue(responseText.contains(commandType.getDescription()));
        }
    }

    @Test
    void testHelpCommandIsCompleted() {
        HelpCommand helpCommand = new HelpCommand();
        assertTrue(helpCommand.isCompleted());
    }
}